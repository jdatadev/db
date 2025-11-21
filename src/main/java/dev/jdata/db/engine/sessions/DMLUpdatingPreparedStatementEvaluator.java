package dev.jdata.db.engine.sessions;

import java.nio.ByteBuffer;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.OverflowException;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;

class DMLUpdatingPreparedStatementEvaluator extends BaseDMLUpdatingEvaluator {

    static void onInsert(SQLInsertStatement insertStatement, DMLUpdatingPreparedEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final PreparedStatementParameters preparedStatementParameters = evaluatorParameter.getPreparedStatementParameters();
        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getColumnsObjectId(insertStatement.getTableName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        storeInsertValues(preparedStatementParameters, insertStatement, evaluatorParameter);
    }

    private static void storeInsertValues(PreparedStatementParameters preparedStatementParameters, SQLInsertStatement insertStatement,
            DMLUpdatingPreparedEvaluatorParameter evaluatorParameter) throws OverflowException {

        final int tableId = evaluatorParameter.getTableId();
        final RowDataNumBits rowDataNumBits = preparedStatementParameters.getParametersRowDataNumBits();
        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final DMLInsertRows insertRows = evaluatorParameter.getInsertRows();

        final int numRows = preparedStatementParameters.getParametersNumRows();

        final InsertRow[] insertRowsArray = evaluatorParameter.allocateInsertRowsArray(numRows);

        final ByteBuffer byteBuffer = preparedStatementParameters.getParametersByteBuffer();

        long byteBufferBitOffset = preparedStatementParameters.getParametersOffset() << 3;

        final int totalNumRowDataBytes = preparedStatementParameters.computeNumBytesPerRow();

        final int totalNumRowDataBits = totalNumRowDataBytes << 3;

        try {
            insertRows.initialize(insertRowsArray, numRows, numRows, rowDataNumBits);

            for (int i = 0; i < numRows; ++ i) {

                insertRowsArray[i].initialize(byteBuffer, byteBufferBitOffset);

                byteBufferBitOffset += totalNumRowDataBits;
            }
        }
        finally {

            evaluatorParameter.freeInsertRowsArray(insertRowsArray);
        }

        insertRows.initialize(null, 1, rowDataNumBits.getNumColumns(), rowDataNumBits);

        evaluatorParameter.getTransaction().insertRows(evaluatorParameter.getTable(), null, insertRows);
    }

    static void onUpdate(SQLUpdateStatement updateStatement, DMLUpdatingPreparedEvaluatorParameter evaluatorParameter)
            throws EvaluateException {

        final PreparedStatementParameters preparedStatementParameters = evaluatorParameter.getPreparedStatementParameters();
        final TableAndColumnNames tableAndColumnNames = evaluatorParameter.getTableAndColumnNames();

        final int tableId = tableAndColumnNames.getColumnsObjectId(updateStatement.getTableName());
        final Table table = tableAndColumnNames.getTable(tableId);

        evaluatorParameter.initializeForDMLUpdateOperation(table);

        final SQLWhereClause whereClause = updateStatement.getWhereClause();

        final IMutableLongLargeArray rowIds = whereClause != null
                ? evaluatorParameter.allocateLargeLongArray()
                : null;

        final int numUpdates = preparedStatementParameters.getParametersNumRows();

        try {
            for (int i = 0; i < numUpdates; ++ i) {

                evaluateUpdate(updateStatement, evaluatorParameter, rowIds);
            }
        }
        finally {

            if (rowIds != null) {

                evaluatorParameter.freeMutableLargeLongArray(rowIds);
            }
        }
    }

    static void onDelete(SQLDeleteStatement sqlDeleteStatement, DMLUpdatingPreparedEvaluatorParameter evaluatorParameter) throws EvaluateException {

        final PreparedStatementParameters preparedStatementParameters = evaluatorParameter.getPreparedStatementParameters();

        final int numDeletes = preparedStatementParameters.getParametersNumRows();

        for (int i = 0; i < numDeletes; ++ i) {

            delete(sqlDeleteStatement, evaluatorParameter);
        }
    }
}
