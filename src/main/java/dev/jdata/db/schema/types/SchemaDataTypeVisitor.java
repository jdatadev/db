package dev.jdata.db.schema.types;

public interface SchemaDataTypeVisitor<T, R, E extends Exception> {

    R onBooleanType(BooleanType schemaDataType, T parameter) throws E;
    R onSmallIntType(SmallIntType schemaDataType, T parameter) throws E;
    R onIntegerType(IntegerType schemaDataType, T parameter) throws E;
    R onBigIntType(BigIntType schemaDataType, T parameter) throws E;
    R onFloatType(FloatType schemaDataType, T parameter) throws E;
    R onDoubleType(DoubleType schemaDataType, T parameter) throws E;
    R onDecimalType(DecimalType schemaDataType, T parameter) throws E;
    R onCharType(CharType schemaDataType, T parameter) throws E;
    R onVarCharType(VarCharType schemaDataType, T parameter) throws E;
    R onDateType(DateType schemaDataType, T parameter) throws E;
    R onTimeType(TimeType schemaDataType, T parameter) throws E;
    R onTimestampType(TimestampType schemaDataType, T parameter) throws E;
    R onBlobType(BlobType schemaDataType, T parameter) throws E;
    R onTextObjectType(TextObjectType schemaDataType, T parameter) throws E;
    R onCustomType(SchemaCustomType schemaDataType, T parameter) throws E;
}
