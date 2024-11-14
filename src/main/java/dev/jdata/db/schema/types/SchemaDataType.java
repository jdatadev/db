package dev.jdata.db.schema.types;

public abstract class SchemaDataType {

 //   private final DBType dbType;

    private final boolean nullable;

    public abstract <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter);

    SchemaDataType(boolean nullable) {

        this.nullable = nullable;
    }

    public final boolean isNullable() {
        return nullable;
    }
}
