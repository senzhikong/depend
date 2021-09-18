package com.senzhikong.db.dialect;

import org.hibernate.boot.Metadata;
import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.schema.spi.Exporter;

public class MySqlDialect extends MySQL8Dialect {

    private final Exporter<ForeignKey> foreignKeyExporter = new Exporter<ForeignKey>() {
        @Override
        public String[] getSqlCreateStrings(ForeignKey exportable, Metadata metadata) {
            return new String[0];
        }

        @Override
        public String[] getSqlDropStrings(ForeignKey exportable, Metadata metadata) {
            return new String[0];
        }
    };

    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }

    public Exporter<ForeignKey> getForeignKeyExporter() {
        return foreignKeyExporter;
    }
}
