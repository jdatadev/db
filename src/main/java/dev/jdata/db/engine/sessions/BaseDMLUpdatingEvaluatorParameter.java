package dev.jdata.db.engine.sessions;

import java.nio.ByteBuffer;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.dml.DMLUpdateRows.UpdateRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.arrays.IArrayAllocator;
import dev.jdata.db.utils.adt.arrays.ICachedMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.ICachedMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.niobuffers.IByteArrayByteBufferAllocator;

abstract class BaseDMLUpdatingEvaluatorParameter extends BaseDMLEvaluatorParameter {

    private final INumStorageBitsGetter numStorageBitsGetter;
    private final IByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
    private final ICachedMutableLongLargeArrayAllocator mutableLongLargeArrayAllocator;

    private final RowDataNumBits rowDataNumBits;

    private final DMLInsertRows insertRows;
    private final DMLUpdateRows updateRows;

    private final UpdateRow[] updateRowsArray;

    private Table table;
    private int tableId;

    private SQLExpressionEvaluator[] expressionEvaluators;

    private int rowBitOffset;

    private int totalNumRowBits;

    abstract void evaluateParameterByIndex(int parameterIndex, SQLExpressionEvaluator dst);

    BaseDMLUpdatingEvaluatorParameter(AllocationType allocationType, IArrayAllocator<SQLExpressionEvaluator> arrayAllocator, INumStorageBitsGetter numStorageBitsGetter,
            IByteArrayByteBufferAllocator byteArrayByteBufferAllocator, ICachedMutableLongLargeArrayAllocator mutableLongLargeArrayAllocator) {
        super(allocationType, arrayAllocator);

        this.numStorageBitsGetter = Objects.requireNonNull(numStorageBitsGetter);
        this.byteArrayByteBufferAllocator = Objects.requireNonNull(byteArrayByteBufferAllocator);
        this.mutableLongLargeArrayAllocator = Objects.requireNonNull(mutableLongLargeArrayAllocator);

        this.rowDataNumBits = new RowDataNumBits(DBConstants.MAX_COLUMNS);

        this.insertRows = new DMLInsertRows();
        this.updateRows = new DMLUpdateRows();

        this.updateRowsArray = new UpdateRow[] {

                new UpdateRow()
        };
    }

    final INumStorageBitsGetter getNumStorageBitsGetter() {
        return numStorageBitsGetter;
    }

    final ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return byteArrayByteBufferAllocator.allocateByteArrayByteBuffer(minimumCapacity);
    }

    final void freeByteArrayByteBuffer(ByteBuffer byteArrayByteBuffer) {

        Objects.requireNonNull(byteArrayByteBuffer);

        byteArrayByteBufferAllocator.freeByteBuffer(byteArrayByteBuffer);
    }

    final ICachedMutableLongLargeArray allocateLargeLongArray(long minimumCapacity) {

        Checks.isLongMinimumCapacity(minimumCapacity);

        return mutableLongLargeArrayAllocator.createMutable(minimumCapacity);
    }

    final void freeMutableLargeLongArray(ICachedMutableLongLargeArray mutableLongLargeArray) {

        Objects.requireNonNull(mutableLongLargeArray);

        mutableLongLargeArray.clear();

        mutableLongLargeArrayAllocator.freeMutable(mutableLongLargeArray);
    }

    final void initializeForDMLUpdateOperation(Table table) {

        this.table = Objects.requireNonNull(table);
        this.tableId = table.getId();
    }

    final Table getTable() {
        return table;
    }

    final int getTableId() {
        return tableId;
    }

    final RowDataNumBits getRowDataNumBits() {
        return rowDataNumBits;
    }

    final DMLInsertRows getInsertRows() {
        return insertRows;
    }

    final DMLUpdateRows getUpdateRows() {
        return updateRows;
    }

    final UpdateRow[] getUpdateRowsArray() {
        return updateRowsArray;
    }

    final SQLExpressionEvaluator[] getExpressionEvaluators() {
        return expressionEvaluators;
    }

    final void setExpressionEvaluators(SQLExpressionEvaluator[] expressionEvaluators) {

        this.expressionEvaluators = Objects.requireNonNull(expressionEvaluators);
    }

    final int getRowBitOffset() {
        return rowBitOffset;
    }

    final void clearRowBitOffset() {

        this.rowBitOffset = 0;
    }

    final void increaseRowBitOffset(int numBits) {

        this.rowBitOffset += Checks.isNumBits(numBits);
    }

    final int getTotalNumRowBits() {
        return totalNumRowBits;
    }

    final void clearTotalNumRowBits() {

        this.totalNumRowBits = 0;
    }

    final void increaseTotalNumRowBits(int numBits) {

        this.rowBitOffset += Checks.isNumBits(numBits);
    }
}
