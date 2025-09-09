package dev.jdata.db.engine.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.Databases;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.operations.IDatabaseOperations;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.ISQLString;

public final class DatabaseServer implements IDatabaseServer {

    private final Databases databases;

    public DatabaseServer(Databases databases) {

        this.databases = Objects.requireNonNull(databases);
    }

    @Override
    public int getDatabase(CharSequence dbName) {

        return databases.getDatabase(dbName);
    }

    @Override
    public int createDatabase(CharSequence dbName, DatabaseParameters parameters) {

        return databases.createDatabase(dbName, parameters);
    }

    @Override
    public int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters) {

        return databases.getOrCreateDatabase(dbName, parameters);
    }

    @Override
    public void dropDatabase(int databaseId) {

        databases.dropDatabase(databaseId);
    }

    @Override
    public int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, ISQLString sqlString) {

        return databases.prepareStatement(databaseId, sessionId, sqlStatement, sqlString);
    }

    @Override
    public void freePreparedStatement(int databaseId, int sessionId, int preparedStatementId) {

        databases.freePreparedStatement(databaseId, sessionId, preparedStatementId);
    }

    @Override
    public IDatabaseOperations getDatabaseOperations(int databaseId) {

        return databases.getDatabaseOperations(databaseId);
    }

    @Override
    public int addSession(int databaseId, Charset charset) {

        return databases.addSession(databaseId, charset);
    }

    @Override
    public void closeSession(int databaseId, int sessionId) {

        databases.closeSession(databaseId, sessionId);
    }

    @Override
    public Charset getSessionCharset(int databaseId, int sessionId) {

        return databases.getSessionCharset(databaseId, sessionId);
    }

    @Override
    public IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId) {

        return databases.getDatabaseSessionStatus(databaseId);
    }

    @Override
    public <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId, PreparedStatementParameters parameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        return databases.executePreparedStatement(databaseId, sessionId, preparedStatementId, parameters, resultWriter);
    }

    @Override
    public long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length) throws IOException {

        return databases.createPreparedStatementLargeObject(databaseId, sessionId, preparedStatementId, length);
    }

    @Override
    public void storePreparedStatementLargeObjectPart(int databaseId, int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer,
            int offset, int length) throws IOException {

        databases.storePreparedStatementLargeObjectPart(databaseId, sessionId, preparedStatementId, largeObjectRef, isFinal, byteBuffer, offset, length);
    }
}
