package dev.jdata.db.common.storagebits;

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

public abstract class BaseMaxNumStorageBitsAdapter extends BaseNumStorageBitsAdapter {

    @Override
    public final Integer onBooleanType(BooleanType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.BOOLEAN_MAX;
    }

    @Override
    public final Integer onSmallIntType(SmallIntType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.SHORT_MAX;
    }

    @Override
    public final Integer onIntegerType(IntegerType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.INT_MAX;
    }

    @Override
    public final Integer onBigIntType(BigIntType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.LONG_MAX;
    }

    @Override
    public final Integer onFloatType(FloatType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.FLOAT_MAX;
    }

    @Override
    public final Integer onDoubleType(DoubleType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.DOUBLE_MAX;
    }

    @Override
    public final Integer onDecimalType(DecimalType schemaDataType, NumStorageBitsParameters parameter) {

        return Integer.MAX_VALUE;
    }

    @Override
    public final Integer onCharType(CharType schemaDataType, NumStorageBitsParameters parameter) {

        return getCharMaxNumBits(schemaDataType.getLength(), parameter);
    }

    @Override
    public final Integer onVarCharType(VarCharType schemaDataType, NumStorageBitsParameters parameter) {

        return getCharMaxNumBits(schemaDataType.getMaxLength(), parameter);
    }

    private static int getCharMaxNumBits(int maxLength, NumStorageBitsParameters parameter) {

        final int result;

        final StorageMode storageMode = parameter.getStringStorageMode();

        switch (storageMode) {

        case INT_REFERENCE:
        case LONG_REFERENCE:

            result = storageMode.getMaxNumBits();
            break;

        case VERBATIM:

            final Integer numBitsPerStringCharacter = parameter.getNumBitsPerStringCharacter();

            Assertions.isNotNull(numBitsPerStringCharacter);

            result = maxLength * numBitsPerStringCharacter;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public final Integer onDateType(DateType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.INT_MAX;
    }

    @Override
    public final Integer onTimeType(TimeType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.INT_MAX;
    }

    @Override
    public final Integer onTimestampType(TimestampType schemaDataType, NumStorageBitsParameters parameter) {

        return NumStorageBits.LONG_MAX;
    }

    @Override
    public final Integer onBlobType(BlobType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getBlobStorageMode().getMaxNumBits();
    }

    @Override
    public final Integer onTextObjectType(TextObjectType schemaDataType, NumStorageBitsParameters parameter) {

        return parameter.getTextStorageMode().getMaxNumBits();
    }
}
