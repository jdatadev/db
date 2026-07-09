package dev.jdata.db.schema.types;

public interface SchemaDataTypeVisitor<P, R, E extends Exception> {

    R onBooleanType(BooleanType schemaDataType, P parameter) throws E;
    R onSmallIntType(SmallIntType schemaDataType, P parameter) throws E;
    R onIntegerType(IntegerType schemaDataType, P parameter) throws E;
    R onBigIntType(BigIntType schemaDataType, P parameter) throws E;
    R onFloatType(FloatType schemaDataType, P parameter) throws E;
    R onDoubleType(DoubleType schemaDataType, P parameter) throws E;
    R onDecimalType(DecimalType schemaDataType, P parameter) throws E;
    R onCharType(CharType schemaDataType, P parameter) throws E;
    R onVarCharType(VarCharType schemaDataType, P parameter) throws E;
    R onDateType(DateType schemaDataType, P parameter) throws E;
    R onTimeType(TimeType schemaDataType, P parameter) throws E;
    R onTimestampType(TimestampType schemaDataType, P parameter) throws E;
    R onBlobType(BlobType schemaDataType, P parameter) throws E;
    R onTextObjectType(TextObjectType schemaDataType, P parameter) throws E;
    R onCustomType(SchemaCustomType schemaDataType, P parameter) throws E;
}
