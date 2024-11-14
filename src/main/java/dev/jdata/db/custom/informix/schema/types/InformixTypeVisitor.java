package dev.jdata.db.custom.informix.schema.types;

public interface InformixTypeVisitor<T, R> {

    R onSerial(SerialType schemaDataType, T parameter);
    R onBigSerial(BigSerialType schemaDataType, T parameter);
}
