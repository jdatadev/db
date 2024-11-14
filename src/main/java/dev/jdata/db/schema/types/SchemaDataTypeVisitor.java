package dev.jdata.db.schema.types;

public interface SchemaDataTypeVisitor<T, R> {

    R onBooleanType(BooleanType schemaDataType, T parameter);
    R onSmallIntType(SmallIntType schemaDataType, T parameter);
    R onIntegerType(IntegerType schemaDataType, T parameter);
    R onBigIntType(BigIntType schemaDataType, T parameter);
    R onFloatType(FloatType schemaDataType, T parameter);
    R onDoubleType(DoubleType schemaDataType, T parameter);
    R onDecimalType(DecimalType schemaDataType, T parameter);
    R onCharType(CharType schemaDataType, T parameter);
    R onVarCharType(VarCharType schemaDataType, T parameter);
    R onDateType(DateType schemaDataType, T parameter);
    R onTimeType(TimeType schemaDataType, T parameter);
    R onTimestampType(TimestampType schemaDataType, T parameter);
    R onBlobType(BlobType schemaDataType, T parameter);
    R onTextObjectType(TextObjectType schemaDataType, T parameter);
    R onCustomType(SchemaCustomType schemaDataType, T parameter);
}
