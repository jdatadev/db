package dev.jdata.db.engine.sessions;

import java.util.function.Function;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.OverflowException;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValue;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValues;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateValues;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.ILongArray;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.checks.Checks;

public class DMLUpdatingStatementEvaluator {

    static void onInsert(SQLInsertStatement insertStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getColumnsObjectId(insertStatement.getTableName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        final RowDataNumBits rowDataNumBits = evaluatorParameter.getRowDataNumBitsAndOffsets();

        Checks.isEmpty(rowDataNumBits);

        final ASTList<Expression> values = insertStatement.getValues();

        evaluteInsertValues(values, insertStatement, evaluatorParameter, (v, s, p) -> storeInsertValues(v, s, p));
    }

    @FunctionalInterface
    private interface EvaluatedInsertValuesProcessor<E extends Exception> {

        void processEvalutedValues(SQLExpressionEvaluator[] values, SQLInsertStatement insertStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws E;
    }

    private static <E extends EvaluateException> void evaluteInsertValues(ASTList<Expression> values, SQLInsertStatement insertStatement,
            DMLUpdatingEvaluatorParameter evaluatorParameter, EvaluatedInsertValuesProcessor<E> insertValuesProcessor) throws EvaluateException {

        final int numColumns = values.size();

        final SQLExpressionEvaluator[] expressionEvaluators = evaluatorParameter.allocateEvaluatorArray(numColumns);

        try {
            evaluatorParameter.setExpressionEvaluators(expressionEvaluators);

            values.foreachWithIndexAndParameter(evaluatorParameter, (v, i, p) -> {

                final SQLExpressionEvaluator expressionEvaluator = p.getExpressionEvaluators()[i];

                expressionEvaluator.evaluate(v, evaluatorParameter);
            });

            insertValuesProcessor.processEvalutedValues(expressionEvaluators, insertStatement, evaluatorParameter);
        }
        finally {

            evaluatorParameter.freeEvaluatorArray(expressionEvaluators, numColumns);
        }
    }

    private static void storeInsertValues(SQLExpressionEvaluator[] values, SQLInsertStatement insertStatement, DMLUpdatingEvaluatorParameter evaluatorParameter)
            throws OverflowException {

        final int tableId = evaluatorParameter.getTableId();
        final RowDataNumBits rowDataNumBits = evaluatorParameter.getRowDataNumBitsAndOffsets();
        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        if (Array.closureOrConstantContains(values, e -> !e.isResolved())) {

            throw new IllegalArgumentException();
        }

        final DMLInsertRows insertRows = evaluatorParameter.getInsertRows();

        final int totalNumRowBytes = ColumnStoreUtil.setTableColumns(tableId, insertStatement.getColumnNames(), insertRows, evaluatorParameter.getInsertRowsArray(),
                rowDataNumBits, tableAndColumnNames, evaluatorParameter.getNumStorageBitsGetter());

        ColumnStoreUtil.performWriteWithOneSourceRow(totalNumRowBytes, values, evaluatorParameter, p -> p.getUpdateRowsArray()[0],
                    (v, i, p) -> ColumnStoreUtil.storeColumnToByteBuffer(v, i, p, e -> e.getInsertRows().getRowBuffer(0).array()));

        evaluatorParameter.getTransaction().insertRows(evaluatorParameter.getTable(), null, insertRows);
    }

    private static void storeColumnToByteBufferExpression(Expression expression, int index, DMLUpdatingEvaluatorParameter evaluatorParameter,
            Function<BaseDMLUpdatingEvaluatorParameter, byte[]> byteArrayGetter) throws EvaluateException {

        final SQLExpressionEvaluator evaluator = evaluatorParameter.getExpressionEvaluator();

        evaluator.evaluate(expression, evaluatorParameter);

        ColumnStoreUtil.storeColumnToByteBuffer(evaluator, index, evaluatorParameter, byteArrayGetter);
    }

    static void onUpdate(SQLUpdateStatement updateStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getColumnsObjectId(updateStatement.getTableName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        final RowDataNumBits rowDataNumBits = evaluatorParameter.getRowDataNumBitsAndOffsets();

        Checks.isEmpty(rowDataNumBits);

        final SQLWhereClause whereClause = updateStatement.getWhereClause();

        final LargeLongArray rowIds = whereClause != null
                ? evaluatorParameter.allocateLargeLongArray()
                : null;

        try {
            if (whereClause != null) {

                ColumnStoreUtil.selectUpdateOrDeleteRows(whereClause, evaluatorParameter, rowIds);
            }

            final SQLUpdateValues updateValues = updateStatement.getValues();

            if (updateValues instanceof SQLColumnValueUpdateValues) {

                final SQLColumnValueUpdateValues columnValueUpdateValues = (SQLColumnValueUpdateValues)updateValues;

                final ASTList<SQLColumnValueUpdateValue> values = columnValueUpdateValues.getValues();

                evaluteUpdateValues(values, columnValueUpdateValues, rowIds, evaluatorParameter, (v, u, r, p) -> storeUpdateValues(v, u, r, p));
            }
            else {
                throw new UnsupportedOperationException();
            }
        }
        finally {

            if (rowIds != null) {

                evaluatorParameter.freeLargeLongArray(rowIds);
            }
        }
    }

    @FunctionalInterface
    private interface EvaluatedUpdateValuesProcessor<E extends Exception> {

        void processEvaluatedValues(SQLExpressionEvaluator[] values, SQLColumnValueUpdateValues updateValues, ILongArray rowIds,
                DMLUpdatingEvaluatorParameter evaluatorParameter) throws OverflowException;
    }

    private static <E extends EvaluateException> void evaluteUpdateValues(ASTList<SQLColumnValueUpdateValue> values, SQLColumnValueUpdateValues updateValues,
            ILongArray rowIds, DMLUpdatingEvaluatorParameter evaluatorParameter, EvaluatedUpdateValuesProcessor<E> evaluatedValuesProcessor) throws EvaluateException {

        final int numColumns = values.size();

        final SQLExpressionEvaluator[] expressionEvaluators = evaluatorParameter.allocateEvaluatorArray(numColumns);

        try {
            evaluatorParameter.setExpressionEvaluators(expressionEvaluators);

            values.foreachWithIndexAndParameter(evaluatorParameter, (v, i, p) -> {

                final SQLExpressionEvaluator expressionEvaluator = p.getExpressionEvaluators()[i];

                expressionEvaluator.evaluate(v.getValue(), evaluatorParameter);
            });

            evaluatedValuesProcessor.processEvaluatedValues(expressionEvaluators, updateValues, rowIds, evaluatorParameter);
        }
        finally {

            evaluatorParameter.freeEvaluatorArray(expressionEvaluators, numColumns);
        }
    }

    private static void storeUpdateValues(SQLExpressionEvaluator[] values, SQLColumnValueUpdateValues updateValues, ILongArray rowIds,
            DMLUpdatingEvaluatorParameter evaluatorParameter) throws OverflowException {

        final Transaction transaction = evaluatorParameter.getTransaction();
        final Table table = evaluatorParameter.getTable();

        if (Array.closureOrConstantContains(values, e -> !e.isResolved())) {

            throw new UnsupportedOperationException();
        }
        else {
            final DMLUpdateRows updateRows = evaluatorParameter.getUpdateRows();

            final int totalNumRowBytes = ColumnStoreUtil.setUpdateTableColumns(updateValues.getValues(), evaluatorParameter);

            ColumnStoreUtil.performWriteWithOneSourceRow(totalNumRowBytes, values, evaluatorParameter, p -> p.getUpdateRowsArray()[0],
                    (v, i, p) -> ColumnStoreUtil.storeColumnToByteBuffer(v, i, p, e -> e.getUpdateRows().getRowBuffer(0).array()));

            if (rowIds != null) {

                transaction.updateRows(table, rowIds, updateRows);
            }
            else {
                transaction.updateAllRows(table, updateRows);
            }
        }
    }

    static void onDelete(SQLDeleteStatement deleteStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getTableId(deleteStatement.getObjectName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        final RowDataNumBits rowDataNumBits = evaluatorParameter.getRowDataNumBitsAndOffsets();

        Checks.isEmpty(rowDataNumBits);

        final SQLWhereClause whereClause = deleteStatement.getWhereClause();

        final LargeLongArray rowIds = whereClause != null
                ? evaluatorParameter.allocateLargeLongArray()
                : null;

        try {
            final Transaction transaction = evaluatorParameter.getTransaction();

            if (whereClause != null) {

                ColumnStoreUtil.selectUpdateOrDeleteRows(whereClause, evaluatorParameter, rowIds);

                transaction.deleteRows(table, rowIds);
            }
            else {
                transaction.deleteAllRows(table);
            }
        }
        finally {

            if (rowIds != null) {

                evaluatorParameter.freeLargeLongArray(rowIds);
            }
        }
    }
}
