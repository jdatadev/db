package dev.jdata.db.schema.types;

public abstract class SchemaDataType {

 //   private final DBType dbType;

    private final boolean nullable;

    SchemaDataType(boolean nullable) {

        this.nullable = nullable;
    }

    public final boolean isNullable() {
        return nullable;
    }

    public final int getMinNumBits() {

        throw new UnsupportedOperationException();
    }

    public final int getMaxNumBits() {

        throw new UnsupportedOperationException();
    }
}
