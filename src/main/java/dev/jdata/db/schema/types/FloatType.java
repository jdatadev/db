package dev.jdata.db.schema.types;

public final class FloatType extends BaseFloatingPointType {

    public static final FloatType NULLABLE = new FloatType(true);
    public static final FloatType NON_NULLABLE = new FloatType(false);

    public static FloatType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private FloatType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onFloatType(this, parameter);
    }
}
