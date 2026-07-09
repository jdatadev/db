package dev.jdata.db.custom.informix.schema.types;

public final class BigSerialType extends BaseSequenceType {

    public static final BigSerialType INSTANCE = new BigSerialType();

    @Override
    public <P, R> R visitInformixType(InformixTypeVisitor<P, R> visitor, P parameter) {

        return visitor.onBigSerial(this, parameter);
    }
}
