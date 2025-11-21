package dev.jdata.db.data.cache.row;

import java.io.DataOutput;
import java.util.Objects;

import dev.jdata.db.data.BaseRows;
import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeDoublyLinkedNodeList;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeSinglyLinkedNodeList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToLongWithRemoveStaticMap;
import dev.jdata.db.utils.checks.Checks;

public final class RowCache extends BaseRows implements DataCache {

    private static final long NO_INDEX = -1L;

    private static final int BYTE_ARRAY_SIZE = 1000 * 1000;

    private final IHeapMutableLongToLongWithRemoveStaticMap indexByRow;
    private final IHeapMutableLongToLongWithRemoveStaticMap insertionOrderNodeByRow;

    private final RowLargeByteArray cache;
    private final long[] numBits;

    private final IMutableLongLargeDoublyLinkedNodeList insertionOrderList;
    private final IMutableLongLargeSinglyLinkedNodeList insertionOrderFreeList;

    public RowCache() {

        final int initialMapCapacity = 0;

        this.indexByRow = IHeapMutableLongToLongWithRemoveStaticMap.create(initialMapCapacity);
        this.insertionOrderNodeByRow = IHeapMutableLongToLongWithRemoveStaticMap.create(initialMapCapacity);

        final int initialCapacity = 1000;

        this.cache = new RowLargeByteArray(initialCapacity);
        this.numBits = new long[initialCapacity];

        final int initialInsertionOrderListOuterCapacity = 1000;

        final int insertOrderListInnerCapacity = 1000 * 1000;

        this.insertionOrderList = IMutableLongLargeDoublyLinkedNodeList.create(initialInsertionOrderListOuterCapacity, insertOrderListInnerCapacity);
        this.insertionOrderFreeList = IMutableLongLargeSinglyLinkedNodeList.create(initialInsertionOrderListOuterCapacity, insertOrderListInnerCapacity);
    }

    @Override
    public boolean writeRowToDataOutput(int tableId, long rowId, DataOutput dataOutput, int[] columnIndices, RowDataNumBitsGetter outputRowDataNumBitsGetter, boolean updated) {

        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);

        Objects.requireNonNull(dataOutput);
        Objects.requireNonNull(outputRowDataNumBitsGetter);

        return false;
    }

    @Override
    public boolean retrieveRow(int tableId, long rowId, byte[] outputBuffer, long outputBufferStartBitOffset, int[] columnIndices,
            RowDataNumBitsGetter outputRowDataNumBitsGetter, boolean updated) {

        checkParameters(tableId, rowId, columnIndices, outputRowDataNumBitsGetter);
        Objects.requireNonNull(outputBuffer);
        Checks.isBufferBitsOffset(outputBufferStartBitOffset);

        final RowDataNumBitsGetter inputRowDataNumBitsGetter = null;

        final int inputNumColumns = inputRowDataNumBitsGetter.getNumColumns();
        final int outputNumColumns = outputRowDataNumBitsGetter.getNumColumns();

        if (inputNumColumns != columnIndices.length) {

            throw new IllegalArgumentException();
        }

        if (outputNumColumns > inputNumColumns) {

            throw new IllegalArgumentException();
        }

        final boolean found;

        final long rowKey = makeHashKey(tableId, rowId);

        final long index = getByteArrayIndex(rowKey);

        if (index != NO_INDEX) {

            cache.retrieveRow(index, outputBuffer, inputRowDataNumBitsGetter, outputBufferStartBitOffset, columnIndices, outputRowDataNumBitsGetter);

            if (!updated) {

                updateInInsertionOrderList(rowKey);
            }

            found = true;
        }
        else {
            found = false;
        }

        return found;
    }

    public void addRow(int tableId, long rowId, byte[] inputBuffer, long inputBufferBitOffset, int[] columnIndices, RowDataNumBitsGetter inputRowDataNumBitsGetter) {

        Checks.isTableId(tableId);
        Checks.isRowId(rowId);
        Objects.requireNonNull(inputBuffer);
        Checks.isBufferBitsOffset(inputBufferBitOffset);
        Objects.requireNonNull(columnIndices);
        Objects.requireNonNull(inputRowDataNumBitsGetter);

        final RowDataNumBitsGetter outputRowDataNumBitsGetter = null;

        final int inputNumColumns = inputRowDataNumBitsGetter.getNumColumns();
        final int outputNumColumns = outputRowDataNumBitsGetter.getNumColumns();

        if (inputNumColumns != columnIndices.length) {

            throw new IllegalArgumentException();
        }

        if (outputNumColumns > inputNumColumns) {

            throw new IllegalArgumentException();
        }

        final long rowKey = makeHashKey(tableId, rowId);

        addToInsertionOrderList(rowKey);
    }

    private void addToInsertionOrderList(long rowKey) {

        final long node = insertionOrderList.addHeadAndReturnNode(rowKey);

        insertionOrderNodeByRow.put(rowKey, node);
    }

    private long removeFromInsertionOrderList() {

        final long rowKey = insertionOrderList.removeTailAndReturnValue();

        insertionOrderNodeByRow.remove(rowKey);

        return rowKey;
    }

    private void updateInInsertionOrderList(long rowKey) {

        final long node = insertionOrderNodeByRow.get(rowKey);

        insertionOrderList.removeNodeAndReturnValue(node);

        addToInsertionOrderList(rowKey);
    }

    private long getByteArrayIndex(int tableId, long rowId) {

        return getByteArrayIndex(makeHashKey(tableId, rowId));
    }

    private long getByteArrayIndex(long rowKey) {

        return indexByRow.get(rowKey);
    }
}
