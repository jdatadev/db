package dev.jdata.db.schema.types;

public final class DoubleType extends BaseFloatingPointType {

    public static final DoubleType NULLABLE = new DoubleType(true);
    public static final DoubleType NON_NULLABLE = new DoubleType(false);

    public static DoubleType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private DoubleType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onDoubleType(this, parameter);
    }
}
