package dev.jdata.db.engine.transactions.mvcc;

import java.util.Objects;

import dev.jdata.db.engine.transactions.RowValue;
import dev.jdata.db.engine.transactions.RowValueType;
import dev.jdata.db.engine.transactions.SelectColumn;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.checks.Checks;

final class ValueComparer implements IClearable {

    private final MutableDecimal scratchMutableDecimal;

    ValueComparer() {

        this.scratchMutableDecimal = new MutableDecimal();
    }

    static boolean computeMatches(SelectColumn selectColumn, int compareResult) {

        final boolean columnMatches;

        switch (selectColumn.getComparisonOperator()) {

        case LESS_THAN:

            columnMatches = compareResult < 0;
            break;

        case LESS_THAN_OR_EQUAL_TO:

            columnMatches = compareResult <= 0;
            break;

        case EQUAL_TO:

            columnMatches = compareResult == 0;
            break;

        case GREATER_THAN:

            columnMatches = compareResult > 0;
            break;

        case GREATER_THAN_OR_EQUALTO:

            columnMatches = compareResult >= 0;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return columnMatches;
    }

    @Override
    public void clear() {

        scratchMutableDecimal.clear();
    }

    int compareAll(TransactionSelect select, SelectColumn selectColumn, BitBuffer mvccBitBuffer, RowValue rowValue, long bufferColumnBitOffset) {

        Objects.requireNonNull(select);
        Objects.requireNonNull(selectColumn);
        Objects.requireNonNull(mvccBitBuffer);
        Objects.requireNonNull(rowValue);
        Checks.isBufferBitsOffset(bufferColumnBitOffset);

        final RowValueType rowValueType = rowValue.getType();

        final int compareResult;

        switch (rowValueType.getDBType()) {

        case BOOLEAN:
        case INTEGER:
        case FLOATING_POINT:

            compareResult = compareSimpleScalar(selectColumn, mvccBitBuffer, bufferColumnBitOffset);
            break;

        case DECIMAL:

            final MutableDecimal bufferDecimal = mvccBitBuffer.getDecimal(bufferColumnBitOffset, scratchMutableDecimal);

            compareResult = rowValue.getDecimal().compareTo(bufferDecimal);
            break;

        case STRING:

            switch (rowValueType) {

            case STRING:

                final long bufferString = mvccBitBuffer.getUnsignedLong(bufferColumnBitOffset);

                compareResult = select.getStringLookup().compare(rowValue.getString(), bufferString);
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return compareResult;
    }

    private static int compareSimpleScalar(SelectColumn selectColumn, BitBuffer mvccBitBuffer, long bufferBitOffset) {

        Objects.requireNonNull(selectColumn);
        Objects.requireNonNull(mvccBitBuffer);
        Checks.isBufferBitsOffset(bufferBitOffset);

        final RowValue rowValue = selectColumn.getRowValue();

        final int result;

        switch (rowValue.getType()) {

        case BOOLEAN:

            final boolean selectColumnValue = rowValue.getBoolean();
            final boolean bufferValue = mvccBitBuffer.getBoolean(bufferBitOffset);

            result = selectColumnValue
                    ? (bufferValue ? 0 : 1)
                    : (bufferValue ? -1 : 0);
            break;

        case SHORT:

            result = Short.compare(rowValue.getShort(), mvccBitBuffer.getSignedShort(bufferBitOffset));
            break;

        case INT:

            result = Integer.compare(rowValue.getInt(), mvccBitBuffer.getSignedInt(bufferBitOffset));
            break;

        case LONG:

            result = Long.compare(rowValue.getLong(), mvccBitBuffer.getSignedLong(bufferBitOffset));
            break;

        case FLOAT:

            result = Float.compare(rowValue.getLong(), mvccBitBuffer.getFloat(bufferBitOffset));
            break;

        case DOUBLE:

            result = Double.compare(rowValue.getLong(), mvccBitBuffer.getDouble(bufferBitOffset));
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }
}
