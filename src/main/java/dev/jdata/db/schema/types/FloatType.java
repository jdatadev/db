package dev.jdata.db.schema.types;

public final class FloatType extends BaseFloatingPointType {

    public static final FloatType NULLABLE = new FloatType(true);
    public static final FloatType NON_NULLABLE = new FloatType(false);

    private FloatType(boolean nullable) {
        super(nullable);
    }
}
