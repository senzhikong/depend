package com.senzhikong.db.dialect;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.schema.spi.Exporter;

/**
 * @author shu
 */
public class MySqlDialect extends MySQLDialect {

    private final Exporter<ForeignKey> foreignKeyExporter = new ForeignKeyExporter();

    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }

    @Override
    public Exporter<ForeignKey> getForeignKeyExporter() {
        return foreignKeyExporter;
    }
}
