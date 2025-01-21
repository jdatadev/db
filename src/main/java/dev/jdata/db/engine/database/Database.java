package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.data.indices.Indices;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.sessions.Session;
import dev.jdata.db.engine.sessions.Sessions;
import dev.jdata.db.engine.statements.SQLStatements;
import dev.jdata.db.engine.statements.SQLStatements.InsertStatement;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.engine.transactions.Transactions;
import dev.jdata.db.engine.transactions.Transactions.TransactionFactory;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.Table;
import dev.jdata.db.utils.adt.arrays.IntArray;
import dev.jdata.db.utils.checks.Checks;

public final class Database<T extends Transaction> implements DataOperations {

    private final DatabaseSchema schema;
    private final DataCache dataCache;

    private final Tables tables;
    private final Indices indices;

    private final SQLStatements sqlStatements;

    private final Sessions sessions;

    private final Transactions<T> transactions;

    Database(DatabaseSchema databaseSchema, DataCache dataCache, long initialTransactionId, long[] initialRowIds, TransactionFactory<T> transactionFactory) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(dataCache);
        Checks.isTransactionId(initialTransactionId);
        Objects.requireNonNull(initialRowIds);
        Objects.requireNonNull(transactionFactory);

        this.schema = databaseSchema;
        this.dataCache = dataCache;

        this.tables = new Tables(databaseSchema, initialRowIds);
        this.indices = new Indices();

        this.sqlStatements = new SQLStatements();

        this.sessions = new Sessions();

        this.transactions = new Transactions<>(initialTransactionId, transactionFactory);
    }

    @Override
    public int startTransaction(int sessionId) {

        return transactions.addTransaction();
    }

    @Override
    public void commitTransaction(int sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollbackTransaction(int sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public int createSavePoint(int sessionId, long savePointName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int rollbackToSavePoint(int sessionId, long savePointName) {
        // TODO Auto-generated method stub
        return 0;
    }

/*
    public Session addSession() {

        return sessions.addSession();
    }
*/

    @Override
    public void insertRows(int sessionId, int sqlStatementId, DMLInsertRows rows) {

        final Session session = sessions.getSession(sessionId);

        final InsertStatement insertStatement = (InsertStatement)sqlStatements.getSQLStatement(sqlStatementId);

        final int tableId = insertStatement.getTableId();

        final Table table = schema.getTable(tableId);

        final IntArray columns = insertStatement.getColumns();

        final int numRows = rows.getNumRows();

        for (int i = 0; i < numRows; ++ i) {

            final byte[] rowBuffer = rows.getRowBuffer(i);
            final long rowBufferBitOffset = rows.getRowBufferBitOffset(i);
        }
    }

    @Override
    public void updateRows(int sessionId, int sqlStatementId, DMLUpdateRows rows) {

    }
}
