package dev.jdata.db.engine.database;

import java.io.IOException;
import java.nio.ByteBuffer;

import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.ISession.IPreparedStatementParameters;

public interface IDatabaseExecutePreparedStatement {

    <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId, IPreparedStatementParameters parameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E;

    long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length) throws IOException;

    void storePreparedStatementLargeObjectPart(int databaseId, int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal,
            ByteBuffer byteBuffer, int offset, int length) throws IOException;
}
