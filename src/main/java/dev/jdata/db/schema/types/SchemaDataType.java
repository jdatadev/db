package dev.jdata.db.schema.types;

public abstract class SchemaDataType {

 //   private final DBType dbType;
/*
    private final boolean nullable;
*/
    public abstract <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E;

/*
    SchemaDataType(boolean nullable) {

        this.nullable = nullable;
    }

    public final boolean isNullable() {
        return nullable;
    }
*/

    @Override
    public String toString() {

        return getClass().getSimpleName() + " []";
    }

    @Override
    public boolean equals(Object object) {

        return object != null && getClass().equals(object.getClass());
    }
}
