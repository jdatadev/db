package dev.jdata.db.storage.backend;

import dev.jdata.db.schema.types.BigIntType;
import dev.jdata.db.schema.types.BlobType;
import dev.jdata.db.schema.types.BooleanType;
import dev.jdata.db.schema.types.CharType;
import dev.jdata.db.schema.types.DateType;
import dev.jdata.db.schema.types.DecimalType;
import dev.jdata.db.schema.types.DoubleType;
import dev.jdata.db.schema.types.FloatType;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.TextObjectType;
import dev.jdata.db.schema.types.TimeType;
import dev.jdata.db.schema.types.TimestampType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.utils.checks.Assertions;

abstract class BaseMinNumStorageBitsAdapter extends BaseNumStorageBitsAdapter {

    @Override
    public Integer onBooleanType(BooleanType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getBooleanMinNumBits();
    }

    @Override
    public final Integer onSmallIntType(SmallIntType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getShortMinNumBits();
    }

    @Override
    public final Integer onIntegerType(IntegerType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getIntMinNumBits();
    }

    @Override
    public final Integer onBigIntType(BigIntType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getLongMinNumBits();
    }

    @Override
    public final Integer onFloatType(FloatType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.FLOAT_MIN;
    }

    @Override
    public final Integer onDoubleType(DoubleType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.DOUBLE_MIN;
    }

    @Override
    public final Integer onDecimalType(DecimalType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.DOUBLE_MAX;
    }

    @Override
    public final Integer onCharType(CharType schemaDataType, NumStorageBitsParameters parameter) {

        return getCharMinNumBits(schemaDataType.getLength(), parameter);
    }

    @Override
    public final Integer onVarCharType(VarCharType schemaDataType, NumStorageBitsParameters parameter) {

        return getCharMinNumBits(schemaDataType.getMinLength(), parameter);
    }

    private static int getCharMinNumBits(int minLength, NumStorageBitsParameters parameter) {

        final int result;

        final StorageMode storageMode = parameter.getStringStorageMode();

        switch (storageMode) {

        case INT_REFERENCE:
        case LONG_REFERENCE:

            result = storageMode.getMinNumBits();
            break;

        case VERBATIM:

            final Integer numBitsPerStringCharacter = parameter.getNumBitsPerStringCharacter();

            Assertions.isNotNull(numBitsPerStringCharacter);

            result = minLength * numBitsPerStringCharacter;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public final Integer onDateType(DateType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getIntMinNumBits();
    }

    @Override
    public final Integer onTimeType(TimeType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getIntMinNumBits();
    }

    @Override
    public final Integer onTimestampType(TimestampType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getLongMinNumBits();
    }

    @Override
    public final Integer onBlobType(BlobType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getBlobStorageMode().getMinNumBits();
    }

    @Override
    public final Integer onTextObjectType(TextObjectType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getTextStorageMode().getMinNumBits();
    }
}
