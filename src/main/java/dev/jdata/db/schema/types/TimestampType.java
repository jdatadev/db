package dev.jdata.db.schema.types;

public final class TimestampType extends TimeBasedType {

    public static final TimestampType NULLABLE = new TimestampType(true);
    public static final TimestampType NON_NULLABLE = new TimestampType(false);

    private TimestampType(boolean nullable) {
        super(nullable);
    }
}
