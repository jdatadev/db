package dev.jdata.db.schema.types;

public abstract class SchemaCustomType extends SchemaDataType {

    @Override
    public final <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onCustomType(this, parameter);
    }
}
