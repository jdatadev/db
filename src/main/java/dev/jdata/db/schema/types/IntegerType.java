package dev.jdata.db.schema.types;

public final class IntegerType extends BaseIntegerType {

    public static final IntegerType NULLABLE = new IntegerType(true);
    public static final IntegerType NON_NULLABLE = new IntegerType(false);

    private IntegerType(boolean nullable) {
        super(nullable);
    }
}
