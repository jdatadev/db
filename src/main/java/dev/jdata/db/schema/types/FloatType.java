package dev.jdata.db.schema.types;

public final class FloatType extends BaseFloatingPointType {

    public static final FloatType INSTANCE = new FloatType();

    private FloatType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onFloatType(this, parameter);
    }
}
