package dev.jdata.db.schema.types;

public final class TimeType extends TimeBasedType {

    public static final TimeType NULLABLE = new TimeType(true);
    public static final TimeType NON_NULLABLE = new TimeType(false);

    public static TimeType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private TimeType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onTimeType(this, parameter);
    }
}
