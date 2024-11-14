package dev.jdata.db.custom.informix.schema.types;

public final class SerialType extends BaseSequenceType {

    public static final SerialType INSTANCE = new SerialType();

    private SerialType() {
        super(false);
    }

    @Override
    public <R, T> R visitInformixType(InformixTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onSerial(this, parameter);
    }
}
