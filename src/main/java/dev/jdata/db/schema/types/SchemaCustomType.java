package dev.jdata.db.schema.types;

public abstract class SchemaCustomType extends SchemaDataType {

    protected SchemaCustomType(boolean nullable) {
        super(nullable);
    }

    @Override
    public final <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onCustomType(this, parameter);
    }
}
