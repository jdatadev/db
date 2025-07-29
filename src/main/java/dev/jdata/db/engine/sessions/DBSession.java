package dev.jdata.db.engine.sessions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.operations.IDatabaseExecuteOperations.ISelectResultWriter;
import dev.jdata.db.engine.database.operations.exceptions.DMLException;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.sql.parse.SQLString;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionConsumer;

public final class DBSession extends BaseDescriptorable<DBSession.SessionState> implements Session, IDatabaseSessionStatus, IClearable {

    private static final boolean DEBUG = DebugConstants.DEBUG_DB_SESSION;

    private static final boolean ASSERT = AssertionContants.ASSERT_DB_SESSION;

    public interface LargeObjectStorer<E extends Exception> {

        long createLargeObject(long length) throws E;

        void addLargeObjectPart(long largeObjectRef, ByteBuffer byteBuffer, int offset, int length) throws E;

        void closeLargeObject(long largeObjectRef) throws E;
    }

    @Deprecated
    interface LargeObjectRetriever<E extends Exception> {

        void retreiveLargeObject(long largeObjectRef, CheckedExceptionConsumer<MappedByteBuffer, E> mappedByteBufferConsumer) throws E;
    }

    private final SQLStatementAdapter<DMLUpdatingEvaluatorParameter, Void, EvaluateException> statementVisitor
            = new SQLStatementAdapter<DMLUpdatingEvaluatorParameter, Void, EvaluateException>() {

        @Override
        public Void onInsert(SQLInsertStatement insertStatement, DMLUpdatingEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingStatementEvaluator.onInsert(insertStatement, parameter);

            return null;
        }

        @Override
        public Void onUpdate(SQLUpdateStatement updateStatement, DMLUpdatingEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingStatementEvaluator.onUpdate(updateStatement, parameter);

            return null;
        }

        @Override
        public Void onDelete(SQLDeleteStatement deleteStatement, DMLUpdatingEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingStatementEvaluator.onDelete(deleteStatement, parameter);

            return null;
        }
    };

    private final SQLStatementAdapter<DMLUpdatingPreparedEvaluatorParameter, Void, EvaluateException> preparedStatementVisitor
            = new SQLStatementAdapter<DMLUpdatingPreparedEvaluatorParameter, Void, EvaluateException>() {

        @Override
        public Void onInsert(SQLInsertStatement insertStatement, DMLUpdatingPreparedEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingPreparedStatementEvaluator.onInsert(insertStatement, parameter);

            return null;
        }

        @Override
        public Void onUpdate(SQLUpdateStatement updateStatement, DMLUpdatingPreparedEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingPreparedStatementEvaluator.onUpdate(updateStatement, parameter);

            return null;
        }

        @Override
        public Void onDelete(SQLDeleteStatement deleteStatement, DMLUpdatingPreparedEvaluatorParameter parameter) throws EvaluateException {

            DMLUpdatingPreparedStatementEvaluator.onDelete(deleteStatement, parameter);

            return null;
        }
    };

    static enum SessionState implements State {

        CREATED(true),
        CONNECTED(false),
        DISCONNECTED(true);

        private final boolean initializable;

        private SessionState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private final PreparedStatements preparedStatements;

    private Charset charset;
    private LargeObjectStorer<IOException> largeObjectStorer;

    private int currentTransaction;

    DBSession() {
        super(SessionState.CREATED, DEBUG);

        this.preparedStatements = new PreparedStatements();
    }

    @Override
    public int getSessionId() {

        return getDescriptor();
    }

    @Override
    public Charset getCharset() {

        return charset;
    }

    @Override
    public int getCurrentTransaction() {

        return currentTransaction;
    }

    @Override
    public IDatabaseSessionStatus getStatus() {

        return this;
    }

    @Override
    public int prepareStatement(BaseSQLStatement sqlStatement, SQLString sqlString) {

        return preparedStatements.addPreparedStatement(sqlStatement, sqlString);
    }

    @Override
    public void freePreparedStatement(int preparedStatementId) {

        Checks.isPreparedStatementId(preparedStatementId);

        preparedStatements.removePreparedStatement(preparedStatementId);
    }

    @Override
    public void close() {

        clear();
    }

    @Override
    public void clear() {

        preparedStatements.clear();
    }

    @Override
    public <E extends Exception> void executeDMLSelectSQL(SQLSelectStatement sqlSelectStatement, DMLSelectEvaluatorParameter evaluatorParameter,
            ISelectResultWriter<E> selectResultWriter) throws DMLException, E {

        Objects.requireNonNull(sqlSelectStatement);
        Objects.requireNonNull(evaluatorParameter);
        Objects.requireNonNull(selectResultWriter);

        throw new UnsupportedOperationException();
    }

    @Override
    public long executeDMUpdatingLStatement(SQLDMLUpdatingStatement sqlDMLUpdatingStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException {

        Objects.requireNonNull(sqlDMLUpdatingStatement);
        Objects.requireNonNull(evaluatorParameter);

        sqlDMLUpdatingStatement.visit(statementVisitor, evaluatorParameter);

        return evaluatorParameter.getNumUpdated();
    }

    @Override
    public <E extends Exception> long executePreparedStatement(int preparedStatementId, PreparedStatementParameters preparedStatementParameters,
            DMLUpdatingPreparedEvaluatorParameter evaluatorParameter, ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException {

        Checks.isPreparedStatementId(preparedStatementId);
        Objects.requireNonNull(preparedStatementParameters);
        Objects.requireNonNull(evaluatorParameter);
        Objects.requireNonNull(resultWriter);

        final PreparedStatement preparedStatement = preparedStatements.getPreparedStatement(preparedStatementId);

        final BaseSQLStatement sqlStatement = preparedStatement.getSQLStatement();

        evaluatorParameter.setPreparedStatementParameters(preparedStatementParameters);

        sqlStatement.visit(preparedStatementVisitor, evaluatorParameter);

        return evaluatorParameter.getResult();
    }

    @Override
    public long createPreparedStatementLargeObjectPart(int preparedStatementId, long length) throws IOException {

        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLengthAboveZero(length);

        return largeObjectStorer.createLargeObject(length);
    }

    @Override
    public void storePreparedStatementLargeObjectPart(int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer, int offset, int length)
            throws IOException {

        try {
            largeObjectStorer.addLargeObjectPart(largeObjectRef, byteBuffer, offset, length);
        }
        finally {

            if (isFinal) {

                largeObjectStorer.closeLargeObject(largeObjectRef);
            }
        }
    }

    void initialize(Charset charset, LargeObjectStorer<IOException> largeObjectStorer) {

        this.charset = Objects.requireNonNull(charset);
        this.largeObjectStorer = Objects.requireNonNull(largeObjectStorer);
    }

    void commitTransaction() {

        clearTransaction();
    }

    void rollbackTransaction() {

        clearTransaction();
    }

    private void clearTransaction() {

        if (currentTransaction == Transaction.NO_TRANSACTION_DESCRIPTOR) {

            throw new IllegalStateException();
        }

        this.currentTransaction = Transaction.NO_TRANSACTION_DESCRIPTOR;
    }
}
