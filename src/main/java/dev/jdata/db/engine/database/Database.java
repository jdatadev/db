package dev.jdata.db.engine.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.engine.database.Database.DatabaseState;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.sessions.Session;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.engine.sessions.Sessions;
import dev.jdata.db.engine.statements.SQLStatements;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.engine.transactions.Transactions;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.parse.SQLParser.SQLString;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.checks.Checks;

public final class Database extends BaseDescriptorable<DatabaseState> implements IDatabaseOperations, IDatabase {

    public interface DMLEvaluatorParameterAllocator {

        DMLUpdatingEvaluatorParameter allocateDMLEvaluatorParameter();

        void freeDMLEvaluatorParameter(DMLUpdatingEvaluatorParameter evaluatorParameter);
    }

    public interface DMLPreparedStatementEvaluatorParameterAllocator {

        DMLUpdatingPreparedEvaluatorParameter allocateDMLPreparedStatementEvaluatorParameter();

        void freeDMLPreparedStatementEvaluatorParameter(DMLUpdatingPreparedEvaluatorParameter evaluatorParameter);
    }

    static enum DatabaseState implements State {

        CREATED(true);

        private final boolean initializable;

        private DatabaseState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private final String name;
    private final IDatabasesAllocators allocators;
    private final StringManagement stringManagement;
    private final DatabaseSchemaManager schemas;
    private final DataCache dataCache;
//    private final TableDataStorageBackend tableDataStorageBackend;
    private final DMLEvaluatorParameterAllocator dmlEvaluatorParameterAllocator;
    private final DMLPreparedStatementEvaluatorParameterAllocator dmlPreparedStatementEvaluatorParameterAllocator;

//    private final Tables tables;
//    private final Indices indices;

    private final SQLStatements sqlStatements;

    private final Sessions sessions;

    private final Transactions transactions;

    Database(String name, DatabaseParameters parameters, DMLEvaluatorParameterAllocator dmlEvaluatorParameterAllocator,
            DMLPreparedStatementEvaluatorParameterAllocator dmlPreparedStatementEvaluatorParameterAllocator) {
        super(DatabaseState.CREATED, false);

        Checks.isDatabaseName(name);
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(dmlEvaluatorParameterAllocator);
        Objects.requireNonNull(dmlPreparedStatementEvaluatorParameterAllocator);

        this.name = name;
        this.allocators = parameters.getAllocators();
        this.stringManagement = parameters.getStringManagement();
        this.schemas = parameters.getDatabaseSchemas();
        this.dataCache = parameters.getDataCache();
//        this.tableDataStorageBackend = parameters;

        this.dmlEvaluatorParameterAllocator = dmlEvaluatorParameterAllocator;
        this.dmlPreparedStatementEvaluatorParameterAllocator = dmlPreparedStatementEvaluatorParameterAllocator;

//        this.tables = new Tables(schemas, parameters.getInitialRowIds());
//        this.indices = new Indices();

        this.sqlStatements = new SQLStatements();

        this.sessions = new Sessions(parameters.getLargeObjectStorer());

        this.transactions = new Transactions(parameters.getInitialTransactionId(), parameters.getTransactionFactory());
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

    @Override
    public int addSession(Charset charset) {

        return sessions.addSession(charset).getSessionId();
    }

    public Charset getSessionCharset(int sessionId) {

        Checks.isSessionDescriptor(sessionId);

        return getSession(sessionId).getCharset();
    }

    @Override
    public void closeSession(int sessionId) {

        try {
            getSession(sessionId).close();
        }
        finally {

            sessions.removeSession(sessionId);
        }
    }

    @Override
    public void executeDDLSQL(int sessionId, BaseSQLDDLOperationStatement sqlStatement) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Exception> void executeDMLSelectSQL(int sessionId, SQLSelectStatement sqlStatement, SelectResultWriter<E> selectResultWriter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public long executeDMLUpdateSQL(int sessionId, SQLDMLUpdatingStatement sqlStatement) throws DMLException {

        final DMLUpdatingEvaluatorParameter evaluatorParameter = dmlEvaluatorParameterAllocator.allocateDMLEvaluatorParameter();

        final Session session = getSession(sessionId);

        final Transaction transaction = transactions.getTransaction(session.getCurrentTransaction());

        evaluatorParameter.initializeForDMLOperation(transaction);

        final long numUpdatedRows;

        try {
            numUpdatedRows = session.executeDMUpdatingLStatement(sqlStatement, evaluatorParameter);
        }
        finally {

            dmlEvaluatorParameterAllocator.freeDMLEvaluatorParameter(evaluatorParameter);
        }

        return numUpdatedRows;
    }

    public String getName() {
        return name;
    }

    int prepareStatement(int sessionId, BaseSQLStatement sqlStatement, SQLString sqlString) {

        Checks.isSessionDescriptor(sessionId);
        Objects.requireNonNull(sqlStatement);
        Objects.requireNonNull(sqlString);

        return getSession(sessionId).prepareStatement(sqlStatement, sqlString);
    }

    <E extends Exception> long executePreparedStatement(int sessionId, int preparedStatementId, PreparedStatementParameters preparedStatementParameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Objects.requireNonNull(preparedStatementParameters);
        Objects.requireNonNull(resultWriter);

        final long result;

        final DMLUpdatingPreparedEvaluatorParameter evaluatorParameter = dmlPreparedStatementEvaluatorParameterAllocator.allocateDMLPreparedStatementEvaluatorParameter();

        try {
            result = getSession(sessionId).executePreparedStatement(preparedStatementId, preparedStatementParameters, evaluatorParameter, resultWriter);
        }
        finally {

            dmlPreparedStatementEvaluatorParameterAllocator.freeDMLPreparedStatementEvaluatorParameter(evaluatorParameter);
        }

        return result;
    }

    long createPreparedStatementLargeObjectPart(int sessionId, int preparedStatementId, long length) throws IOException {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);

        return getSession(sessionId).createPreparedStatementLargeObjectPart(preparedStatementId, length);
    }

    void storePreparedStatementLargeObjectPart(int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer, int offset, int length)
            throws IOException {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLargeObjectRef(largeObjectRef);
        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isNumBytes(length);

        getSession(sessionId).storePreparedStatementLargeObjectPart(preparedStatementId, largeObjectRef, isFinal, byteBuffer, offset, length);
    }

    void freePreparedStatement(int sessionId, int preparedStatementId) {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);

        getSession(sessionId).freePreparedStatement(preparedStatementId);
    }

    int getDatabaseId() {

        return getDescriptor();
    }

    private Session getSession(int sessionId) {

        return sessions.getSession(sessionId);
    }
}
