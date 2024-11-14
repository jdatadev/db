package dev.jdata.db.schema.types;

public final class SerialType extends BaseSequenceType {

    public static final SerialType INSTANCE = new SerialType();

    private SerialType() {
        super(false);
    }
}
