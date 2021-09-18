package com.senzhikong.db.dialect;

import org.hibernate.boot.Metadata;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.tool.schema.spi.Exporter;

public class ForeignKeyExporter implements Exporter<ForeignKey> {

    @Override
    public String[] getSqlCreateStrings(ForeignKey exportable, Metadata metadata) {
        return new String[0];
    }

    @Override
    public String[] getSqlDropStrings(ForeignKey exportable, Metadata metadata) {
        return new String[0];
    }
}
