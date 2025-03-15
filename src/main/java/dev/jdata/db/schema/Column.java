package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.checks.Checks;

public final class Column {

    private final String name;
    private final SchemaDataType schemaType;
    private final boolean nullable;

    public Column(String name, SchemaDataType schemaType, boolean nullable) {

        this.name = Checks.isColumnName(name);
        this.schemaType = Objects.requireNonNull(schemaType);
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public SchemaDataType getSchemaType() {
        return schemaType;
    }

    public boolean isNullable() {

        return nullable;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [name=" + name + ", schemaType=" + schemaType + ", nullable=" + nullable + "]";
    }
}
