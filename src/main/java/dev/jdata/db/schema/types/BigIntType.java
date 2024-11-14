package dev.jdata.db.schema.types;

public final class BigIntType extends BaseIntegerType {

    public static final BigIntType NULLABLE = new BigIntType(true);
    public static final BigIntType NON_NULLABLE = new BigIntType(false);

    private BigIntType(boolean nullable) {
        super(nullable);
    }
}
