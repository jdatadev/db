package dev.jdata.db.schema.types;

public final class TimeType extends TimeBasedType {

    public static final TimeType NULLABLE = new TimeType(true);
    public static final TimeType NON_NULLABLE = new TimeType(false);

    private TimeType(boolean nullable) {
        super(nullable);
    }
}
