package dev.jdata.db.schema.types;

public final class DoubleType extends BaseFloatingPointType {

    public static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDoubleType(this, parameter);
    }
}
