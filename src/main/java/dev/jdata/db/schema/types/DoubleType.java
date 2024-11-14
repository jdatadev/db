package dev.jdata.db.schema.types;

public final class DoubleType extends BaseFloatingPointType {

    public static final DoubleType NULLABLE = new DoubleType(true);
    public static final DoubleType NON_NULLABLE = new DoubleType(false);

    private DoubleType(boolean nullable) {
        super(nullable);
    }
}
