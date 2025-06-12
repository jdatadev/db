package dev.jdata.db.schema.types;

public final class FloatType extends BaseFloatingPointType {

    public static final FloatType INSTANCE = new FloatType();
/*
    public static final FloatType NULLABLE = new FloatType(true);
    public static final FloatType NON_NULLABLE = new FloatType(false);

    public static FloatType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private FloatType(boolean nullable) {
        super(nullable);
    }
*/
    private FloatType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onFloatType(this, parameter);
    }
}
