package dev.jdata.db.schema.types;

public abstract class SchemaCustomType extends SchemaDataType {

    @Override
    public final <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onCustomType(this, parameter);
    }
}
