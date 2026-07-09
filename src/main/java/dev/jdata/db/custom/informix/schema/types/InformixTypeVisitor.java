package dev.jdata.db.custom.informix.schema.types;

public interface InformixTypeVisitor<P, R> {

    R onSerial(SerialType schemaDataType, P parameter);
    R onBigSerial(BigSerialType schemaDataType, P parameter);
}
