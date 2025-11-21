package dev.jdata.db.engine.transactions.mvcc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.DebugConstants;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLInsertUpdateRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.transactions.ITransactionSharedStateMarker;
import dev.jdata.db.engine.transactions.TransactionMechanism;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.IHeapMutableIntArray;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongArray;
import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.adt.buffers.BufferUtil;
import dev.jdata.db.utils.adt.byindex.ILongByIndexView;
import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedElementsView;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeDoublyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.adt.maps.IHeapMutableIntToLongWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToLongWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IBaseMutableLongSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class MVCCTransaction extends TransactionMechanism<MVCCTransaction.MVCCTransactionState> implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_MVCC_TRANSACTION;

    private static final boolean ASSERT = AssertionContants.ASSERT_MVCC;

    private static final Class<?> debugClass = MVCCTransaction.class;

    private static final long NO_NODE = LargeNodeLists.NO_LONG_NODE;

    public static final class MVCCTransactionState implements ITransactionSharedStateMarker {

        @Override
        public String toString() {

            return getClass().getSimpleName() + " []";
        }
    }

    private static enum DMLOperation {

        INSERT,
        UPDATE,
/*
        UPDATE_PREVIOUS_TRANSACTIONS,
        UPDATE_INSERTED_THIS_TRANSACTION,
        UPDATE_UPDATED_THIS_TRANSACTION,
*/
        UPDATE_ALL,

        DELETE,
        DELETE_ALL;


        public static DMLOperation ofOrdinal(int ordinal) {

            return DMLOperation.values()[ordinal];
        }
    }

    private static final int DML_OPERATION_NUM_BITS;

    static {

        DML_OPERATION_NUM_BITS = BitsUtil.getNumEnumBits(DMLOperation.class);

        Assertions.isLessThanOrEqualTo(DML_OPERATION_NUM_BITS, 3);
    }

    private static final int BIT_OFFSET_NUM_BITS = Long.SIZE - DML_OPERATION_NUM_BITS;

    private static final int DML_OPERATION_SHIFT = BIT_OFFSET_NUM_BITS;
    private static final long DML_OPERATION_MASK = BitsUtil.maskLong(DML_OPERATION_NUM_BITS, DML_OPERATION_SHIFT);

    private static final long BIT_OFFSET_MASK = BitsUtil.maskLong(BIT_OFFSET_NUM_BITS, 0);

    private static final int OPERATION_SEQUENCE_NO_NUM_BITS = Long.SIZE - DML_OPERATION_NUM_BITS;
    private static final long OPERATION_SEQUENCE_NO_MASK = BitsUtil.maskLong(OPERATION_SEQUENCE_NO_NUM_BITS, 0);

    private static final long MAX_BIT_OFFSET = BitsUtil.getMaxLong(BIT_OFFSET_NUM_BITS);

    private boolean[] hasUpdates;
    private int transactionDesciptor;
    private long transactionId;

    private long originatingTransactionId;

    private DBIsolationLevel isolationLevel;

    private final IHeapMutableLongArray rowOperationBySequenceNo;

    private final IHeapMutableIntToLongWithRemoveStaticMap operationListsHeadNodesByTableId;
    private final IHeapMutableIntToLongWithRemoveStaticMap operationListsTailNodesByTableId;
    private final IMutableLongLargeDoublyLinkedMultiHeadNodeList<MVCCTransaction> tableOperationsLists;

    private final BitBuffer insertRows;
    private final BitBuffer updateRows;
    private final BitBuffer updateAllRows;
/*
    private final BitBuffer updateToExistingRows;
    private final BitBuffer updateToInsertRows;
*/
    private final BitBuffer deleteRows;
    private final IHeapMutableIntArray deleteAllRows;

    private final RowBufferComparer rowBufferComparer;

    private long scratcHeadNode;
    private long scratcTailNode;

    private int operationSequenceNoAllocator;

    public MVCCTransaction() {
        this(20);
    }

    MVCCTransaction(int innerCapacityExponent) {

        Checks.isIntCapacityExponent(innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("innerCapacityExponent", innerCapacityExponent));
        }

        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        this.rowOperationBySequenceNo = IHeapMutableLongArray.create();

        this.operationListsHeadNodesByTableId = IHeapMutableIntToLongWithRemoveStaticMap.create(0);
        this.operationListsTailNodesByTableId = IHeapMutableIntToLongWithRemoveStaticMap.create(0);
        this.tableOperationsLists = IMutableLongLargeDoublyLinkedMultiHeadNodeList.create(1, innerCapacity);

        this.insertRows = new BitBuffer(AllocationType.HEAP, innerCapacityExponent);
        this.updateRows = new BitBuffer(AllocationType.HEAP, innerCapacityExponent);
        this.updateAllRows = new BitBuffer(AllocationType.HEAP, innerCapacityExponent);

/*
        this.updateToExistingRows = new MVCCBitBuffer(innerCapacity);
        this.updateToInsertRows = new MVCCBitBuffer(innerCapacity);
*/
        this.deleteRows = new BitBuffer(AllocationType.HEAP, innerCapacityExponent);
        this.deleteAllRows = IHeapMutableIntArray.create(1, DBConstants.NO_TABLE_ID);

        this.rowBufferComparer = new RowBufferComparer();

        this.operationSequenceNoAllocator = 0;

        if (DEBUG) {

            exit();
        }
    }

    public boolean hasUpdates(int tableId) {

        Checks.isTableId(tableId);

        return hasUpdates[tableId];
    }

    public boolean select(TransactionSelect select, BufferedRows commitedRows, IBaseMutableLongSet addedRowIdsDst, IBaseMutableLongSet removedRowIdsDst) {

        Objects.requireNonNull(select);
        Checks.isNull(commitedRows, hasUpdates(select.getTableId()));
        Objects.requireNonNull(addedRowIdsDst);
        Objects.requireNonNull(removedRowIdsDst);

        if (DEBUG) {

            enter(b -> b.add("select", select).add("commitedRows", commitedRows).add("addedRowIdsDst", addedRowIdsDst).add("removedRowIdsDst", removedRowIdsDst));
        }

        final long headNode = operationListsHeadNodesByTableId.get(select.getTableId());

        boolean done = false;

        boolean removeAll = false;

        final long noNode = NO_NODE;

        if (headNode != noNode) {

            for (long node = headNode; !done && node != noNode; node = tableOperationsLists.getNextNode(node)) {

                final long encodedOperation = tableOperationsLists.getValue(node);

                final DMLOperation dmlOperation = decodeOperation(encodedOperation);
                final long bitOffset = decodeOperationByteBufferBitOffset(encodedOperation);

                if (DEBUG) {

                    debug("dml operation from table operations list", b -> b.add("dmlOperation", dmlOperation).add("bitOffset", bitOffset));
                }

                switch (dmlOperation) {

                case INSERT:

                    rowBufferComparer.compareRowForInsertOperation(select, insertRows, bitOffset, addedRowIdsDst);
                    break;

                case UPDATE:

                    rowBufferComparer.compareRowForUpdateOperation(select, updateRows, bitOffset, commitedRows, addedRowIdsDst, removedRowIdsDst);
                    break;

                case UPDATE_ALL:

                    rowBufferComparer.compareRowForUpdateAllOperation(select, updateAllRows, bitOffset, commitedRows, addedRowIdsDst, removedRowIdsDst);
                    break;

                case DELETE: {

                    long deleteRowsBitOffset = bitOffset;

                    final long numRowIds = deleteRows.getUnsignedLong(deleteRowsBitOffset);

                    deleteRowsBitOffset += Long.SIZE;

                    for (long i = 0L; i < numRowIds; ++ i) {

                        final long rowId = deleteRows.getUnsignedLong(deleteRowsBitOffset);

                        deleteRowsBitOffset += Long.SIZE;

                        addedRowIdsDst.removeAtMostOne(rowId);
                        removedRowIdsDst.addUnordered(rowId);
                    }
                    break;
                }

                case DELETE_ALL:

                    removeAll = true;

                    done = true;
                    break;

                default:
                    throw new UnsupportedOperationException();
                }
            }
        }

        if (DEBUG) {

            exit(removeAll);
        }

        return removeAll;
    }

    @Override
    public OperationResult insertRows(MVCCTransactionState mvccSharedState, Table table, int statementId, ILongByIndexOrderedElementsView rowIds, DMLInsertRows rows) {

        checkParameters(mvccSharedState, table, statementId, rowIds, rows);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState).add("table", table).add("statementId", statementId).add("rowIds", rowIds).add("rows", rows));
        }

        addInsertUpdateRows(table.getId(), DMLOperation.INSERT, insertRows, rowIds, rows);

        final OperationResult result = OperationResult.SUCCESS;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public OperationResult updateRows(MVCCTransactionState mvccSharedState, Table table, int statementId, ILongByIndexOrderedElementsView rowIds, DMLUpdateRows rows) {

        checkParameters(mvccSharedState, table, statementId, rowIds, rows);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState).add("table", table).add("statementId", statementId).add("rowIds", rowIds).add("rows", rows));
        }

        final int tableId = table.getId();

        hasUpdates[tableId] = true;

        addInsertUpdateRows(tableId, DMLOperation.UPDATE, updateRows, rowIds, rows);

        final OperationResult result = OperationResult.SUCCESS;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public OperationResult updateAllRows(MVCCTransactionState mvccSharedState, Table table, int statementId, DMLUpdateRows row) {

        checkParameters(mvccSharedState, table, statementId, row);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState).add("table", table).add("statementId", statementId).add("rows", row));
        }

        final int tableId = table.getId();

        hasUpdates[tableId] = true;

        addUpdateAllRows(tableId, DMLOperation.UPDATE_ALL, updateRows, row);

        final OperationResult result = OperationResult.SUCCESS;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public OperationResult deleteRows(MVCCTransactionState mvccSharedState, Table table, int statementId, ILongByIndexOrderedElementsView rowIds) {

        checkParameters(mvccSharedState, table, statementId, rowIds);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState).add("table", table).add("statementId", statementId).add("rowIds", rowIds));
        }

        final long deleteRowBitOffset = deleteRows.getBitOffset();

        addRowOperation(table.getId(), DMLOperation.DELETE, deleteRowBitOffset);

        final long numRowIds = rowIds.getIndexLimit();

        deleteRows.addUnsignedLong(numRowIds);

        for (long i = 0L; i < numRowIds; ++ i) {

            deleteRows.addUnsignedLong(rowIds.get(i));
        }

        final OperationResult result = OperationResult.SUCCESS;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public OperationResult deleteAllRows(MVCCTransactionState mvccSharedState, Table table, int statementId) {

        checkParameters(mvccSharedState, table, statementId);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState).add("table", table).add("statementId", statementId));
        }

        final int tableId = table.getId();

        addRowOperation(tableId, DMLOperation.DELETE_ALL, -1L);

        deleteAllRows.add(tableId);

        final OperationResult result = OperationResult.SUCCESS;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public void commit(MVCCTransactionState mvccSharedState) {

        Objects.requireNonNull(mvccSharedState);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState));
        }

        releaseTransaction();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void rollback(MVCCTransactionState mvccSharedState) {

        Objects.requireNonNull(mvccSharedState);

        if (DEBUG) {

            enter(b -> b.add("mvccSharedState", mvccSharedState));
        }

        releaseTransaction();

        if (DEBUG) {

            exit();
        }
    }

    void prepare(int numTables) {

        Checks.isNotNegative(numTables);

        if (hasUpdates == null || numTables != hasUpdates.length) {

            this.hasUpdates = new boolean[numTables];
        }
    }

    void initialize(int transactionDecriptor, long originatingTransactionId, DBIsolationLevel isolationLevel) {

        if (DEBUG) {

            enter(b -> b.add("transactionDecriptor", transactionDecriptor).add("originatingTransactionId", originatingTransactionId).add("isolationLevel", isolationLevel));
        }

        this.transactionDesciptor = Checks.isTransactionDescriptor(transactionDecriptor);
        this.originatingTransactionId = originatingTransactionId != DBConstants.NO_TRANSACTION_ID
                ? Checks.isTransactionId(originatingTransactionId)
                : originatingTransactionId;
        this.isolationLevel = Objects.requireNonNull(isolationLevel);

        Arrays.fill(hasUpdates, false);

        if (DEBUG) {

            exit();
        }
    }

    long getTransactionId() {
        return transactionId;
    }

    void setTransactionId(long transactionId) {

        if (DEBUG) {

            enter(b -> b.add("transactionId", transactionId));
        }

        this.transactionId = Checks.isTransactionId(transactionId);

        if (DEBUG) {

            exit();
        }
    }

    long getOriginatingTransactionId() {
        return originatingTransactionId;
    }

    DBIsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    private void releaseTransaction() {

        if (DEBUG) {

            enter();
        }

        clear();

        if (DEBUG) {

            exit();
        }
    }

    private void clear() {

        if (DEBUG) {

            enter();
        }

        this.transactionId = DBConstants.NO_TRANSACTION_ID;
        this.originatingTransactionId = DBConstants.NO_TRANSACTION_ID;

        rowOperationBySequenceNo.clear();

        final ILongNodeSetter<MVCCTransaction> noop = (i, n) -> { };

        operationListsHeadNodesByTableId.forEachKeyAndValue(this, (k, v, t) -> t.tableOperationsLists.clear(t, v, noop, noop));

        operationListsHeadNodesByTableId.clear();
        operationListsTailNodesByTableId.clear();

        insertRows.clear();
        updateRows.clear();
        updateAllRows.clear();
/*
        private final MVCCBitBuffer updateToExistingRows;
        private final MVCCBitBuffer updateToInsertRows;
        private final MVCCBitBuffer updateToUpdatedRows;
*/
        deleteRows.clear();

        if (DEBUG) {

            exit();
        }
    }

    private long addRowOperation(int tableId, DMLOperation dmlOperation, long value) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("dmlOperation", dmlOperation).add("value", value));
        }

        final int operationSequenceNo = allocateOperationSequenceNo();

        final long result = addRowOperation(this, tableId, dmlOperation, operationSequenceNo, rowOperationBySequenceNo, operationListsHeadNodesByTableId,
                operationListsTailNodesByTableId, tableOperationsLists, value);

        if (DEBUG) {

            exitWithBinary(result);
        }

        return result;
    }

    private int allocateOperationSequenceNo() {

        return operationSequenceNoAllocator ++;
    }

    private void addUpdateAllRows(int tableId, DMLOperation dmlOperation, BitBuffer mvccBitBuffer, DMLInsertUpdateRows<?> rows) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("dmlOperation", dmlOperation).add("mvccBitBuffer", mvccBitBuffer).add("rows", rows));
        }

        addInsertUpdateRowsImpl(tableId, dmlOperation, mvccBitBuffer, null, rows, rows.getNumElements());

        if (DEBUG) {

            exit();
        }
    }

    private void addInsertUpdateRows(int tableId, DMLOperation dmlOperation, BitBuffer mvccBitBuffer, ILongByIndexOrderedElementsView rowIds, DMLInsertUpdateRows<?> rows) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("dmlOperation", dmlOperation).add("mvccBitBuffer", mvccBitBuffer).add("rowIds", rowIds).add("rows", rows));
        }

        final long numRowIds = rowIds.getIndexLimit();

        Checks.areEqual(numRowIds, rows.getNumElements());

        addInsertUpdateRowsImpl(tableId, dmlOperation, mvccBitBuffer, rowIds, rows, numRowIds);

        if (DEBUG) {

            exit();
        }
    }

    private void addInsertUpdateRowsImpl(int tableId, DMLOperation dmlOperation, BitBuffer mvccBitBuffer, ILongByIndexOrderedElementsView rowIds, DMLInsertUpdateRows<?> rows,
            long numRows) {

        if (DEBUG) {

            enter(b -> b.add("tableId", tableId).add("dmlOperation", dmlOperation).add("mvccBitBuffer", mvccBitBuffer).add("rowIds", rowIds).add("rows", rows)
                    .add("numRows", numRows));
        }

        final long bitBufferEncoded = addRowOperation(tableId, dmlOperation, mvccBitBuffer.getBitOffset());

        if (DEBUG) {

            debug("add encoded row operation", b -> b.add("tableId", tableId).add("dmlOperation", dmlOperation).add("numRows", numRows)
                    .binary("bitBufferEncoded", bitBufferEncoded));
        }

        addInsertUpdateRowsImpl(mvccBitBuffer, rowIds, rows, numRows);

        if (DEBUG) {

            exit();
        }
    }

    private static void addInsertUpdateRowsImpl(BitBuffer mvccBitBuffer, ILongByIndexView rowIds, DMLInsertUpdateRows<?> rows, long numRows) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("mvccBitBuffer", mvccBitBuffer).add("rowIds", rowIds).add("rows", rows).add("numRows", numRows));
        }

        final int numTableColumns = rows.getNumTableColumns();

        if (DEBUG) {

            PrintDebug.debug(debugClass, "add columns", b -> b.add("numTableColumns", numTableColumns));
        }

        mvccBitBuffer.addUnsignedShort(numTableColumns);

        final RowDataNumBits rowDataNumBits = rows.getRowDataNumBits();

        for (int i = 0; i < numTableColumns; ++ i) {

            final int closureI = i;

            final int tableColumn = rows.getTableColumn(i);
            final int numBits = rowDataNumBits.getNumBits(i);

            if (DEBUG) {

                PrintDebug.debug(debugClass, "add column", b -> b.add("i", closureI).add("tableColumn", tableColumn).add("numBits", numBits));
            }

            mvccBitBuffer.addUnsignedShort(tableColumn);
            mvccBitBuffer.addUnsignedShort(numBits);
        }

        if (DEBUG) {

            PrintDebug.debug(debugClass, "add rows", b -> b.add("numRows", numRows));
        }

        mvccBitBuffer.addUnsignedLong(numRows);

        for (int i = 0; i < numRows; ++ i) {

            final int closureI = i;

            if (rowIds != null) {

                final long rowId = rowIds.get(i);

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add rowId", b -> b.add("i", closureI).add("rowId", rowId));
                }

                mvccBitBuffer.addUnsignedLong(rowId);
            }

            final ByteBuffer rowBuffer = rows.getRowBuffer(i);
            final long rowBufferBitOffset = rows.getRowBufferBitOffset(i);

            for (int j = 0; j < numTableColumns; ++ j) {

                if (DEBUG) {

                    final int closureJ = j;

                    PrintDebug.debug(debugClass, "add row column", b -> b.add("i", closureI).add("j", closureJ).add("rowBuffer", BufferUtil.toString(rowBuffer))
                            .add("rowBufferBitOffset", rowBufferBitOffset));
                }

                mvccBitBuffer.addBytes(rowBuffer, rowBufferBitOffset, rowDataNumBits.getNumBits(j));
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    private static long addRowOperation(MVCCTransaction mvccTransaction, int tableId, DMLOperation dmlOperation, int operationSequenceNo,
            IMutableLongArray rowOperationBySequenceNo, IMutableIntToLongWithRemoveStaticMap operationsHeadNodeByTableId,
            IMutableIntToLongWithRemoveStaticMap operationsTailNodeByTableId, IMutableLongLargeDoublyLinkedMultiHeadNodeList<MVCCTransaction> operationsLists, long value) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("mvccTransaction", mvccTransaction).add("tableId", tableId).add("dmlOperation", dmlOperation)
                    .add("operationSequenceNo", operationSequenceNo).add("rowOperationBySequenceNo", rowOperationBySequenceNo).add("value", value));
        }

        if (ASSERT) {

            Assertions.areEqual(operationSequenceNo, rowOperationBySequenceNo.getLimit());
        }

        final long encoded = encodeOperation(dmlOperation, value);

        rowOperationBySequenceNo.add(encoded);

        final long noNode = NO_NODE;

        final long headNode = operationsHeadNodeByTableId.get(tableId);
        final long tailNode = headNode != noNode
                ? operationsTailNodeByTableId.get(tableId)
                : noNode;

        operationsLists.addTail(mvccTransaction, encoded, headNode, tailNode, (t, n) -> t.scratcHeadNode = n, (t, n) -> t.scratcTailNode = n);

        operationsHeadNodeByTableId.put(tableId, mvccTransaction.scratcHeadNode);
        operationsTailNodeByTableId.put(tableId, mvccTransaction.scratcTailNode);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, encoded);
        }

        return encoded;
    }

    private static long encodeOperation(DMLOperation dmlOperation, long byteBufferBitOffset) {

        Checks.isWithinRangeInclusive(byteBufferBitOffset, 0L, MAX_BIT_OFFSET);

        return (((long)dmlOperation.ordinal()) << DML_OPERATION_SHIFT) | byteBufferBitOffset;
    }
/*
    private static int decodeOperationSequenceNo(long value) {

        return Integers.checkUnsignedLongToUnsignedInt(value & OPERATION_SEQUENCE_NO_MASK);
    }
*/

    private static long decodeOperationByteBufferBitOffset(long value) {

        return Integers.checkUnsignedLongToUnsignedInt(value & BIT_OFFSET_MASK);
    }

    private static DMLOperation decodeOperation(long value) {

        return DMLOperation.ofOrdinal(Integers.checkUnsignedLongToUnsignedInt((value & DML_OPERATION_MASK) >> DML_OPERATION_SHIFT));
    }
}
