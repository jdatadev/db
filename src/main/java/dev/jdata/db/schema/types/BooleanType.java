package dev.jdata.db.schema.types;

public final class BooleanType extends ScalarType {

    public static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onBooleanType(this, parameter);
    }
}
