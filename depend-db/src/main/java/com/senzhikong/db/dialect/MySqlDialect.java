package com.senzhikong.db.dialect;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.schema.spi.Exporter;

/**
 * @author shu
 */
public class MySqlDialect extends MySQL8Dialect {

    private final Exporter<ForeignKey> foreignKeyExporter =  new ForeignKeyExporter();

    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }

    @Override
    public Exporter<ForeignKey> getForeignKeyExporter() {
        return foreignKeyExporter;
    }
}
