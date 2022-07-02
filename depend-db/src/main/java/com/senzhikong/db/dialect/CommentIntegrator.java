package com.senzhikong.db.dialect;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.*;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @author shu
 */
@Component
public class CommentIntegrator implements Integrator {
    public static final CommentIntegrator INSTANCE = new CommentIntegrator();

    public CommentIntegrator() {
        super();
    }

    /**
     * Perform comment integration.
     *
     * @param metadata        The "compiled" representation of the mapping information
     * @param sessionFactory  The session factory being created
     * @param serviceRegistry The session factory's service registry
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        processComment(metadata);
    }

    /**
     * Not used.
     *
     * @param sessionFactoryImplementor     The session factory being closed.
     * @param sessionFactoryServiceRegistry That session factory's service registry
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,
            SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    }

    /**
     * Process comment annotation.
     *
     * @param metadata process annotation of this {@code Metadata}.
     */
    private void processComment(Metadata metadata) {
        for (PersistentClass persistentClass : metadata.getEntityBindings()) {
            Class<?> clz = persistentClass.getMappedClass();
            if (clz.isAnnotationPresent(Comment.class)) {
                Comment comment = clz.getAnnotation(Comment.class);
                persistentClass.getTable().setComment(comment.value());
            }
            Property identifierProperty = persistentClass.getIdentifierProperty();
            if (identifierProperty != null) {
                fieldComment(persistentClass, identifierProperty.getName());
            } else {
                org.hibernate.mapping.Component component = persistentClass.getIdentifierMapper();
                if (component != null) {
                    Iterator<Property> iterator = component.getPropertyIterator();
                    while (iterator.hasNext()) {
                        fieldComment(persistentClass, iterator.next().getName());
                    }
                }
            }
            Iterator<Property> iterator = persistentClass.getPropertyIterator();
            while (iterator.hasNext()) {
                Property property = iterator.next();
                String columnName = property.getName();
                fieldComment(persistentClass, columnName);
                Value val = property.getValue();
                if (val instanceof ManyToOne) {
                    joinFieldComment(property, persistentClass);
                }
            }
        }
    }

    private void joinFieldComment(Property property, PersistentClass persistentClass) {
        Value val = property.getValue();
        Iterator<Selectable> columnIterator = val.getColumnIterator();
        while (columnIterator.hasNext()) {
            Selectable selectable = columnIterator.next();
            String name = selectable.getText();
            String comment = getColumnComment(persistentClass, property.getName());
            setFieldComment(name, comment, persistentClass);
        }
    }

    private String getColumnComment(PersistentClass persistentClass, String columnName) {
        Field field = null;
        Class<?> clz = persistentClass.getMappedClass();
        try {
            field = clz.getDeclaredField(columnName);
        } catch (Exception e) {
            while (clz.getSuperclass() != null) {
                clz = clz.getSuperclass();
                try {
                    field = clz.getDeclaredField(columnName);
                } catch (NoSuchFieldException ignore) {
                }
            }
        }
        if (field == null) {
            return null;
        }
        if (!field.isAnnotationPresent(Comment.class)) {
            return null;
        }
        return field.getAnnotation(Comment.class).value();
    }

    private void setFieldComment(String columnName, String comment, PersistentClass persistentClass) {
        Iterator<org.hibernate.mapping.Column> columnIterator = persistentClass.getTable().getColumnIterator();
        while (columnIterator.hasNext()) {
            org.hibernate.mapping.Column column = columnIterator.next();
            if (columnName.equalsIgnoreCase(column.getName().replace("_", ""))) {
                column.setComment(comment);
                break;
            }
        }
    }

    /**
     * Process @{code comment} annotation of field.
     *
     * @param persistentClass Hibernate {@code PersistentClass}
     * @param columnName      name of field
     */
    private void fieldComment(PersistentClass persistentClass, String columnName) {
        Field field = null;
        Class<?> clz = persistentClass.getMappedClass();
        try {
            field = clz.getDeclaredField(columnName);
        } catch (Exception e) {
            while (clz.getSuperclass() != null) {
                clz = clz.getSuperclass();
                try {
                    field = clz.getDeclaredField(columnName);
                } catch (NoSuchFieldException ignore) {
                }
            }
        }
        if (field == null) {
            return;
        }
        if (!field.isAnnotationPresent(Comment.class)) {
            return;
        }
        if (field.isAnnotationPresent(Column.class)) {
            String annotationName = field.getAnnotation(Column.class).name().trim();
            if (!annotationName.isEmpty()) {
                columnName = annotationName;
            }
        }
        String comment = field.getAnnotation(Comment.class).value();
        setFieldComment(columnName, comment, persistentClass);
    }
}
