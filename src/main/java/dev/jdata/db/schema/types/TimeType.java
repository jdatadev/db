package dev.jdata.db.schema.types;

public final class TimeType extends TimeBasedType {

    public static final TimeType INSTANCE = new TimeType();

/*
    public static final TimeType NULLABLE = new TimeType(true);
    public static final TimeType NON_NULLABLE = new TimeType(false);

    public static TimeType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private TimeType(boolean nullable) {
        super(nullable);
    }
*/

    private TimeType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onTimeType(this, parameter);
    }
}
