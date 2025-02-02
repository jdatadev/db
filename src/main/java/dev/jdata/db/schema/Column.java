package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.types.SchemaDataType;

public final class Column {

    private final SchemaDataType schemaType;
    private final boolean nullable;

    public Column(SchemaDataType schemaType, boolean nullable) {

        this.schemaType = Objects.requireNonNull(schemaType);
        this.nullable = nullable;
    }

    public SchemaDataType getSchemaType() {
        return schemaType;
    }

    public boolean isNullable() {

        return nullable;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [schemaType=" + schemaType + ", nullable=" + nullable + "]";
    }
}
