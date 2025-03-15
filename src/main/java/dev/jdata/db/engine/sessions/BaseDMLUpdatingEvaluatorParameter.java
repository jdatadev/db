package dev.jdata.db.engine.sessions;

import java.nio.ByteBuffer;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.data.RowDataNumBitsAndOffsets;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.dml.DMLUpdateRows.UpdateRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.schema.Table;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.allocators.IByteArrayByteBufferAllocator;
import dev.jdata.db.utils.allocators.IArrayAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseDMLUpdatingEvaluatorParameter extends BaseDMLEvaluatorParameter {

    private final NumStorageBitsGetter numStorageBitsGetter;
    private final IByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
    private final ILargeLongArrayAllocator largeLongArrayAllocator;

    private final RowDataNumBitsAndOffsets rowDataNumBitsAndOffsets;

    private final DMLInsertRows insertRows;
    private final DMLUpdateRows updateRows;

    private final UpdateRow[] updateRowsArray;

    private Table table;
    private int tableId;

    private SQLExpressionEvaluator[] expressionEvaluators;

    private int rowBitOffset;

    private int totalNumRowBits;

    abstract void evaluateParameterByIndex(int parameterIndex, SQLExpressionEvaluator dst);

    BaseDMLUpdatingEvaluatorParameter(IArrayAllocator<SQLExpressionEvaluator> arrayAllocator, NumStorageBitsGetter numStorageBitsGetter,
            IByteArrayByteBufferAllocator byteArrayByteBufferAllocator, ILargeLongArrayAllocator largeLongArrayAllocator) {
        super(arrayAllocator);

        this.numStorageBitsGetter = Objects.requireNonNull(numStorageBitsGetter);
        this.byteArrayByteBufferAllocator = Objects.requireNonNull(byteArrayByteBufferAllocator);
        this.largeLongArrayAllocator = Objects.requireNonNull(largeLongArrayAllocator);

        this.rowDataNumBitsAndOffsets = new RowDataNumBitsAndOffsets(DBConstants.MAX_COLUMNS);

        this.insertRows = new DMLInsertRows();
        this.updateRows = new DMLUpdateRows();

        this.updateRowsArray = new UpdateRow[] {

                new UpdateRow()
        };
    }

    final NumStorageBitsGetter getNumStorageBitsGetter() {
        return numStorageBitsGetter;
    }

    final ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity) {

        Checks.isCapacity(minimumCapacity);

        return byteArrayByteBufferAllocator.allocateByteArrayByteBuffer(minimumCapacity);
    }

    final void freeByteArrayByteBuffer(ByteBuffer byteArrayByteBuffer) {

        Objects.requireNonNull(byteArrayByteBuffer);

        byteArrayByteBufferAllocator.freeByteBuffer(byteArrayByteBuffer);
    }

    final LargeLongArray allocateLargeLongArray() {

        return largeLongArrayAllocator.allocateLargeLongArray();
    }

    final void freeLargeLongArray(LargeLongArray largeLongArray) {

        Objects.requireNonNull(largeLongArray);

        largeLongArray.clear();

        largeLongArrayAllocator.freeLargeLongArray(largeLongArray);
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

    @Deprecated // only RowDataNumBits?
    final RowDataNumBitsAndOffsets getRowDataNumBitsAndOffsets() {
        return rowDataNumBitsAndOffsets;
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
