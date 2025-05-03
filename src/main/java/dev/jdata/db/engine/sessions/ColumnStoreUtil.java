package dev.jdata.db.engine.sessions;

import java.nio.ByteBuffer;
import java.util.function.Function;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.data.RowDataNumBitsAndOffsets;
import dev.jdata.db.dml.DMLInsertUpdateRows;
import dev.jdata.db.dml.DMLInsertUpdateRows.InsertUpdateRow;
import dev.jdata.db.dml.DMLUpdateRows.UpdateRow;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.OverflowException;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValue;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

class ColumnStoreUtil {

    @FunctionalInterface
    interface ForEachExpressionValue<E extends Exception> {

        void each(SQLExpressionEvaluator expressionEvaluator, int index, BaseDMLUpdatingEvaluatorParameter evaluatorParameter) throws E;
    }

    static <E extends EvaluateException> void performWriteWithOneSourceRow(int totalNumRowBytes, SQLExpressionEvaluator[] values,
            BaseDMLUpdatingEvaluatorParameter evaluatorParameter, Function<BaseDMLUpdatingEvaluatorParameter, InsertUpdateRow> insertUpdateRowGetter,
            ForEachExpressionValue<E> forEachExpressionValue) throws E {

        final ByteBuffer byteBuffer = evaluatorParameter.allocateByteArrayByteBuffer(totalNumRowBytes);

        try {
            insertUpdateRowGetter.apply(evaluatorParameter).initialize(byteBuffer, 0L);

            final int numValues = values.length;

            for (int i = 0; i < numValues; ++ i) {

                forEachExpressionValue.each(values[i], i, evaluatorParameter);
            }
        }
        finally {

            evaluatorParameter.freeByteArrayByteBuffer(byteBuffer);
        }
    }

    static long selectUpdateOrDeleteRows(SQLWhereClause whereClause, BaseDMLUpdatingEvaluatorParameter evaluatorParameter, LargeLongArray rowIdsDst) {

        final Transaction transaction = evaluatorParameter.getTransaction();
        final Table table = evaluatorParameter.getTable();

        return WhereClauseEvalutor.evaluateWhereClause(whereClause, transaction, table, rowIdsDst);
    }

    static <T extends InsertUpdateRow> int setTableColumns(int tableId, SQLColumnNames sqlcolumnNames, DMLInsertUpdateRows<T> insertUpdateRows, T[] rows,
            RowDataNumBits rowDataNumBits, TableAndColumnNames columnNames, INumStorageBitsGetter numStorageBitsGetter) {

        final int numColumnNames = Integers.checkUnsignedLongToUnsignedInt(sqlcolumnNames.getNumElements());

        return setTableColumnsByIndex(tableId, sqlcolumnNames, numColumnNames, insertUpdateRows, rows, rowDataNumBits, columnNames, numStorageBitsGetter, SQLColumnNames::get);
    }

    @FunctionalInterface
    private interface ColumnNameGetter<T> {

        long getColumName(T columnNames, int index);
    }

    private static <T extends InsertUpdateRow, U> int setTableColumnsByIndex(int tableId, U sqlColumnNames, int numColumnNames, DMLInsertUpdateRows<T> insertUpdateRows, T[] rows,
            RowDataNumBits rowDataNumBits, TableAndColumnNames columnNames, INumStorageBitsGetter numStorageBitsGetter, ColumnNameGetter<U> columnNameGetter) {

        Checks.isExactlyOne(rows.length);

        insertUpdateRows.initialize(rows, 1, numColumnNames, rowDataNumBits);

        int totalNumRowBits = 0;

        for (int i = 0; i < numColumnNames; ++ i) {

            final long sqlColumnName = columnNameGetter.getColumName(sqlColumnNames, i);

            final int numColumnBits = setTableColumn(tableId, i, sqlColumnName, insertUpdateRows, columnNames, numStorageBitsGetter);

            totalNumRowBits += numColumnBits;
        }

        final int totalNumRowBytes = BitBufferUtil.numBytes(totalNumRowBits);

        return totalNumRowBytes;
    }

    static int setUpdateTableColumns(ASTList<SQLColumnValueUpdateValue> updateValues, BaseDMLUpdatingEvaluatorParameter evaluatorParameter) {

        final int tableId = evaluatorParameter.getTableId();
        final UpdateRow[] rows = evaluatorParameter.getUpdateRowsArray();
        final RowDataNumBits rowDataNumBits = evaluatorParameter.getRowDataNumBitsAndOffsets();

        Checks.isExactlyOne(rows.length);

        final int numColumns = updateValues.size();

        evaluatorParameter.getUpdateRows().initialize(rows, 1, numColumns, rowDataNumBits);

        evaluatorParameter.clearTotalNumRowBits();

        updateValues.foreachWithIndexAndParameter(evaluatorParameter, (v, i, p) -> {

            final int numColumnBits = setTableColumn(tableId, i, v.getColumnName(), p.getUpdateRows(), p.getTableAndColumnNames(), p.getNumStorageBitsGetter());

            p.increaseTotalNumRowBits(numColumnBits);
        });

        final int totalNumRowBits = evaluatorParameter.getTotalNumRowBits();

        final int totalNumRowBytes = BitBufferUtil.numBytes(totalNumRowBits);

        return totalNumRowBytes;
    }

    private static int setTableColumn(int tableId, int i, long sqlColumnName, DMLInsertUpdateRows<?> insertUpdateRows, TableAndColumnNames columnNames,
            INumStorageBitsGetter numStorageBitsGetter) {

        final int columnIndex = columnNames.getColumnIndex(tableId, sqlColumnName);

        insertUpdateRows.setColumnMapping(i, columnIndex);

        final SchemaDataType schemaDataType = columnNames.getSchemaDataType(tableId, columnIndex);

        final int numColumnBits = numStorageBitsGetter.getMaxNumBits(schemaDataType);

        return numColumnBits;
    }

    static void storeColumnToByteBuffer(SQLExpressionEvaluator evaluator, int index, BaseDMLUpdatingEvaluatorParameter evaluatorParameter,
            Function<BaseDMLUpdatingEvaluatorParameter, byte[]> byteArrayGetter) throws OverflowException {

        final TableAndColumnNames columnNames = evaluatorParameter.getTableAndColumnNames();

        final RowDataNumBitsAndOffsets rowDataNumBitsAndOffsets = evaluatorParameter.getRowDataNumBitsAndOffsets();

        final int tableId = evaluatorParameter.getTableId();

        final SchemaDataType schemaDataType = columnNames.getSchemaDataType(tableId, evaluatorParameter.getInsertRows().getTableColumn(index));

        final int numBits = evaluator.getNumBits(schemaDataType, evaluatorParameter.getNumStorageBitsGetter());

        final int rowBitOffset = evaluatorParameter.getRowBitOffset();

        rowDataNumBitsAndOffsets.addNumBitsAndOffset(numBits, rowBitOffset);

        evaluatorParameter.increaseRowBitOffset(numBits);

        final byte[] byteArray = byteArrayGetter.apply(evaluatorParameter);

        evaluator.store(byteArray, rowBitOffset, numBits);
    }
}
