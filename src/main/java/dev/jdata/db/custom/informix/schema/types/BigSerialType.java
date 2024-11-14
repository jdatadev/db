package dev.jdata.db.custom.informix.schema.types;

public final class BigSerialType extends BaseSequenceType {

    public static final BigSerialType INSTANCE = new BigSerialType();

    private BigSerialType() {
        super(false);
    }

    @Override
    public <R, T> R visitInformixType(InformixTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onBigSerial(this, parameter);
    }
}
