package dev.jdata.db.storage.backend.tabledata.file;

import java.util.Objects;

import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileColumn;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public class RowBitsUtil {

    public static void clearRow(StorageTableFileSchema storageTableFileSchema, long rowId, long transactionId, byte[] outputRowBuffer, long outputRowBufferBitOffset) {

        Objects.requireNonNull(storageTableFileSchema);
        Checks.isRowId(rowId);
        Checks.isTransactionId(transactionId);
        Checks.isBufferBitsOffset(outputRowBufferBitOffset);

        final int numBitsAdded = addRowHeader(storageTableFileSchema, rowId, true, transactionId, outputRowBuffer, outputRowBufferBitOffset);

        final int numRemainingRowBits = storageTableFileSchema.getTotalNumRowBits() - numBitsAdded;

        BitBufferUtil.clearBits(outputRowBuffer, outputRowBufferBitOffset + numBitsAdded, numRemainingRowBits);
    }

    public static int addRowHeader(StorageTableFileSchema storageTableFileSchema, long rowId, boolean deleteMarker, long transactionId, byte[] outputRowBuffer,
            long outputRowBufferBitOffset) {

        Objects.requireNonNull(storageTableFileSchema);
        Checks.isRowId(rowId);
        Checks.isTransactionId(transactionId);
        Checks.isBufferBitsOffset(outputRowBufferBitOffset);

        long bitOffset = outputRowBufferBitOffset;

        final int numRowIdBits = storageTableFileSchema.getNumRowIdBits();

        bitOffset += BitBufferUtil.setLongValue(outputRowBuffer, rowId, false, bitOffset, numRowIdBits);

        final int numDeleteMarkerBits = storageTableFileSchema.getNumDeleteMarkerBits();

        BitBufferUtil.setBits(outputRowBuffer, deleteMarker, bitOffset, numDeleteMarkerBits);

        bitOffset += numDeleteMarkerBits;

        bitOffset += addRowHeaderAfterDeleteMarker(storageTableFileSchema, transactionId, outputRowBuffer, bitOffset);

        return Integers.checkUnsignedLongToUnsignedInt(bitOffset - outputRowBufferBitOffset);
    }

    public static int addRowHeaderAfterDeleteMarker(StorageTableFileSchema storageTableFileSchema, long transactionId, byte[] outputRowBuffer, long outputRowBufferBitOffset) {

        Objects.requireNonNull(storageTableFileSchema);
        Checks.isTransactionId(transactionId);
        Checks.isBufferBitsOffset(outputRowBufferBitOffset);

        final int numTransactionIdBits = storageTableFileSchema.getNumTransactionIdBits();

        BitBufferUtil.setLongValue(outputRowBuffer, transactionId, false, outputRowBufferBitOffset, numTransactionIdBits);

        return numTransactionIdBits;
    }

    public static int addRowData(StorageTableFileSchema storageTableFileSchema, byte[] inputRowBuffer, long inputRowBufferBitOffset, byte[] outputRowBuffer,
            int outputRowBufferBitOffset, RowDataNumBitsGetter rowDataNumBitsGetter) {

        Objects.requireNonNull(storageTableFileSchema);
        Objects.requireNonNull(inputRowBuffer);
        Checks.isBufferBitsOffset(outputRowBufferBitOffset);
        Objects.requireNonNull(rowDataNumBitsGetter);

        final int numColumns = storageTableFileSchema.getNumColumns();

        final int rowIdBitOffset = outputRowBufferBitOffset;
        final int transactionIdBitOffset = rowIdBitOffset + storageTableFileSchema.getNumRowIdBits();
        final int nullValueBitMapBitOffset = transactionIdBitOffset + storageTableFileSchema.getNumTransactionIdBits();
        final int rowBufferColumnBitOffset = nullValueBitMapBitOffset + storageTableFileSchema.getNumNullValueBitMapBits();

        int numBitsAdded = 0;

        for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

            final StorageTableFileColumn storageColumn = storageTableFileSchema.getStorageColumn(columnIndex);

            final int outputNumBits = storageColumn.getNumBits();

            final int nullValueBitmapIndex = storageTableFileSchema.getNullValueBitmapIndex(columnIndex);

            final boolean isNull;

            if (nullValueBitmapIndex != StorageTableFileSchema.NO_NULL_VALUE_BITMAP_INDEX) {

                isNull = rowDataNumBitsGetter.isNull(columnIndex);

                BitBufferUtil.setBitValue(outputRowBuffer, isNull, nullValueBitMapBitOffset + nullValueBitmapIndex);
            }
            else {
                isNull = false;
            }

            if (isNull) {

                BitBufferUtil.clearBits(outputRowBuffer, inputRowBufferBitOffset, outputNumBits);
            }
            else {
                final int inputNumBits = rowDataNumBitsGetter.getNumBits(columnIndex);

                BitBufferUtil.copyBits(inputRowBuffer, inputRowBufferBitOffset, inputNumBits, inputRowBuffer, rowBufferColumnBitOffset + numBitsAdded, outputNumBits);
            }

            numBitsAdded += outputNumBits;
        }

        return numBitsAdded;
    }
}
