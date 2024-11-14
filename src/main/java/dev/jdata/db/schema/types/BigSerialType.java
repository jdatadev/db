package dev.jdata.db.schema.types;

public final class BigSerialType extends BaseSequenceType {

    public static final BigSerialType INSTANCE = new BigSerialType();

    private BigSerialType() {
        super(false);
    }
}
