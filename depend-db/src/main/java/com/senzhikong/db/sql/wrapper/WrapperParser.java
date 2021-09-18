package com.senzhikong.db.sql.wrapper;

import com.senzhikong.db.dialect.Comment;
import com.senzhikong.db.sql.CacheColumn;
import com.senzhikong.db.sql.CacheTable;
import com.senzhikong.util.string.StringUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

public class WrapperParser {

    private static final Map<String, CacheTable> cacheTableMap = new HashMap<>();

    public static CacheColumn getColumn(Class<?> clz, String field) {
        CacheColumn cacheColumn = getTable(clz).getColumns().get(field);
        if (cacheColumn == null) {
            throw new RuntimeException(String.format("类【%s】不存在%s属性", clz.getName(), field));
        }
        return cacheColumn;
    }

    public static CacheTable getTable(Class<?> clz) {
        CacheTable cacheTable = cacheTableMap.get(clz.getName());
        if (cacheTable != null) {
            return cacheTable;
        }
        cacheTable = new CacheTable();
        String tableName = clz.getSimpleName();
        cacheTable.setClassName(tableName);
        cacheTable.setFullClass(clz.getName());
        cacheTable.setTableClass(clz);
        cacheTable.setName(tableName);
        Table table = clz.getDeclaredAnnotation(Table.class);
        Comment comment = clz.getDeclaredAnnotation(Comment.class);
        if (table != null) {
            String name = table.name();
            String catalog = table.catalog();
            String schema = table.schema();
            if (StringUtil.isNotEmpty(name)) {
                cacheTable.setName(name);
            }
            if (StringUtil.isNotEmpty(catalog)) {
                cacheTable.setCatalog(catalog);
            }
            if (StringUtil.isNotEmpty(schema)) {
                if (schema.startsWith("`")) {
                    schema = schema.substring(1, schema.length() - 1);
                }
                cacheTable.setSchema(schema);
            }
        }
        if (comment != null) {
            cacheTable.setComment(comment.value());
        }
        cacheTable.setColumns(new HashMap<>());
        parseTableColumns(clz, cacheTable);
        cacheTableMap.put(cacheTable.getFullClass(), cacheTable);
        return cacheTable;
    }

    private static void parseTableColumns(Class<?> clz, CacheTable cacheTable) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            CacheColumn cacheColumn = new CacheColumn();
            cacheColumn.setFieldName(field.getName());
            cacheColumn.setFieldClass(field.getType());
            cacheColumn.setFieldType(field.getType().getSimpleName());
            cacheColumn.setColumnName(field.getName());
            Column column = field.getDeclaredAnnotation(Column.class);
            Comment comment = field.getDeclaredAnnotation(Comment.class);
            if (column != null) {
                cacheColumn.setColumnName(column.name());
            }
            if (comment != null) {
                cacheColumn.setComment(comment.value());
            }
            cacheColumn.setTable(cacheTable);
            cacheTable.getColumns().put(cacheColumn.getFieldName(), cacheColumn);
        }
    }

    public static String methodToFieldName(String getName) {
        return getName.substring(3, 4).toLowerCase() +
                getName.substring(4);
    }

    public static <T extends Serializable, S extends Serializable> CacheColumn getColumn(ObjectFunction<T, S> column) {
        try {

            Method method = column.getClass().getDeclaredMethod("writeReplace");
            AccessController.doPrivileged(
                    new SetAccessibleAction<>(method));
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(column);

            String methodName = serializedLambda.getImplMethodName();
            String className = serializedLambda.getImplClass();
            className = className.replaceAll("/", ".");
            String fieldName = methodToFieldName(methodName);
            Class<?> clz = Class.forName(className);
            return getColumn(clz, fieldName);

        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }
}
