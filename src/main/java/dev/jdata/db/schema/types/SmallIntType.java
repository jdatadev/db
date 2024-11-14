package dev.jdata.db.schema.types;

public final class SmallIntType extends BaseIntegerType {

    public static final SmallIntType NULLABLE = new SmallIntType(true);
    public static final SmallIntType NON_NULLABLE = new SmallIntType(false);

    private SmallIntType(boolean nullable) {
        super(nullable);
    }
}
