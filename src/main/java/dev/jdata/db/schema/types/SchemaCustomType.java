package dev.jdata.db.schema.types;

public abstract class SchemaCustomType extends SchemaDataType {
/*
    protected SchemaCustomType(boolean nullable) {
        super(nullable);
    }
*/
    @Override
    public final <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onCustomType(this, parameter);
    }
}
