package dev.jdata.db.schema.types;

public final class DateType extends TimeBasedType {

    public static final DateType NULLABLE = new DateType(true);
    public static final DateType NON_NULLABLE = new DateType(false);

    private DateType(boolean nullable) {
        super(nullable);
    }
}
