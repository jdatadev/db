package dev.jdata.db.custom.informix.schema.types;

public final class SerialType extends BaseSequenceType {

    public static final SerialType INSTANCE = new SerialType();

    @Override
    public <P, R> R visitInformixType(InformixTypeVisitor<P, R> visitor, P parameter) {

        return visitor.onSerial(this, parameter);
    }
}
