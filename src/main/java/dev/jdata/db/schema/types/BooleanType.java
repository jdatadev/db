package dev.jdata.db.schema.types;

public final class BooleanType extends ScalarType {

    public static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onBooleanType(this, parameter);
    }
}
