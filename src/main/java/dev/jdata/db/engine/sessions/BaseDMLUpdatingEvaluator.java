package dev.jdata.db.engine.sessions;

import java.util.Objects;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.OverflowException;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.schema.Table;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValue;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValues;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateValues;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.ILongArrayGetters;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;

class BaseDMLUpdatingEvaluator {

    static void evaluateUpdate(SQLUpdateStatement sqlUpdateStatement, BaseDMLUpdatingEvaluatorParameter evaluatorParameter, LargeLongArray rowIds) throws EvaluateException {

        Objects.requireNonNull(sqlUpdateStatement);
        Objects.requireNonNull(evaluatorParameter);
        Objects.requireNonNull(rowIds);

        final SQLWhereClause sqlWhereClause = sqlUpdateStatement.getWhereClause();

        if (sqlWhereClause != null) {

            ColumnStoreUtil.selectUpdateOrDeleteRows(sqlWhereClause, evaluatorParameter, rowIds);
        }

        final SQLUpdateValues updateValues = sqlUpdateStatement.getValues();

        if (updateValues instanceof SQLColumnValueUpdateValues) {

            final SQLColumnValueUpdateValues columnValueUpdateValues = (SQLColumnValueUpdateValues)updateValues;

            final ASTList<SQLColumnValueUpdateValue> values = columnValueUpdateValues.getValues();

            evaluteUpdateValues(values, columnValueUpdateValues, rowIds, evaluatorParameter, (v, u, r, p) -> storeUpdateValues(v, u, r, p));
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    @FunctionalInterface
    private interface EvaluatedUpdateValuesProcessor<E extends Exception> {

        void processEvaluatedValues(SQLExpressionEvaluator[] values, SQLColumnValueUpdateValues updateValues, ILongArrayGetters rowIds,
                BaseDMLUpdatingEvaluatorParameter evaluatorParameter) throws OverflowException;
    }

    private static <E extends EvaluateException> void evaluteUpdateValues(ASTList<SQLColumnValueUpdateValue> values, SQLColumnValueUpdateValues updateValues,
            ILongArrayGetters rowIds, BaseDMLUpdatingEvaluatorParameter evaluatorParameter, EvaluatedUpdateValuesProcessor<E> evaluatedValuesProcessor)
                    throws EvaluateException {

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

    private static void storeUpdateValues(SQLExpressionEvaluator[] values, SQLColumnValueUpdateValues updateValues, ILongArrayGetters rowIds,
            BaseDMLUpdatingEvaluatorParameter evaluatorParameter) throws OverflowException {

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

    static void delete(SQLDeleteStatement sqlDeleteStatement, BaseDMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getTableId(sqlDeleteStatement.getObjectName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        final SQLWhereClause whereClause = sqlDeleteStatement.getWhereClause();

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
