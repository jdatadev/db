package dev.jdata.db.schema.types;

public abstract class SchemaDataType {

    public abstract <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E;

    @Override
    public String toString() {

        return getClass().getSimpleName() + " []";
    }

    @Override
    public boolean equals(Object object) {

        return object != null && getClass().equals(object.getClass());
    }
}
