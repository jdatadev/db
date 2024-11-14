package dev.jdata.db.schema.types;

public final class BooleanType extends ScalarType {

    public static final BooleanType NULLABLE = new BooleanType(true);
    public static final BooleanType NON_NULLABLE = new BooleanType(false);

    private BooleanType(boolean nullable) {
        super(nullable);
    }
}
