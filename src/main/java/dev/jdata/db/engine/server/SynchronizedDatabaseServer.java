package dev.jdata.db.engine.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.operations.IDatabaseOperations;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLString;

@Deprecated // does this make sense?
final class SynchronizedDatabaseServer implements IDatabaseServer {

    private final IDatabaseServer delegate;

    SynchronizedDatabaseServer(IDatabaseServer delegate) {

        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public synchronized int getDatabase(CharSequence dbName) {
        return delegate.getDatabase(dbName);
    }

    @Override
    public synchronized int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters) {
        return delegate.getOrCreateDatabase(dbName, parameters);
    }

    @Override
    public synchronized int addSession(int databaseId, Charset charset) {
        return delegate.addSession(databaseId, charset);
    }

    @Override
    public synchronized int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, SQLString sqlString) {
        return delegate.prepareStatement(databaseId, sessionId, sqlStatement, sqlString);
    }

    @Override
    public synchronized void freePreparedStatement(int databaseId, int sessionId, int preparedStatementId) {
        delegate.freePreparedStatement(databaseId, sessionId, preparedStatementId);
    }

    @Override
    public synchronized IDatabaseOperations getDatabaseOperations(int databaseId) {
        return delegate.getDatabaseOperations(databaseId);
    }

    @Override
    public int createDatabase(CharSequence dbName, DatabaseParameters parameters) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void dropDatabase(int databaseId) {
        // TODO Auto-generated method stub

    }

    @Override
    public <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId,
            PreparedStatementParameters parameters, ExecuteSQLResultWriter<E> resultWriter)
            throws EvaluateException, E {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length)
            throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void storePreparedStatementLargeObjectPart(int databaseId, int sessionId, int preparedStatementId,
            long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer, int offset, int length) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void closeSession(int databaseId, int sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Charset getSessionCharset(int databaseId, int sessionId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId) {
        // TODO Auto-generated method stub
        return null;
    }
}
