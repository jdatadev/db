package dev.jdata.db.engine.sessions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.JDBCType;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.operations.IDatabaseExecuteOperations.ISelectResultWriter;
import dev.jdata.db.engine.database.operations.exceptions.DMLException;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.bits.BitBufferUtil;

public interface Session {

    public interface PreparedStatementParameters {

        RowDataNumBits getParametersRowDataNumBits();

        int getParametersReferredToNumBytes();

        int getParametersNumRows();

        int getParametersNumColumns();

        JDBCType getParametersJDBCColumnType(int columnIndex);

        ByteBuffer getParametersByteBuffer();

        int getParametersOffset();

        int getParametersLength();

        default int computeNumBytesPerRow() {

            return BitBufferUtil.numBytesExact(getParametersRowDataNumBits().getTotalNumRowDataBits());
        }
    }

    int getSessionId();

    Charset getCharset();

    int getCurrentTransaction();

    IDatabaseSessionStatus getStatus();

    <E extends Exception> void executeDMLSelectSQL(SQLSelectStatement sqlSelectStatement, DMLSelectEvaluatorParameter eEvaluatorParameter,
            ISelectResultWriter<E> selectResultWriter) throws DMLException, E;

    long executeDMUpdatingLStatement(SQLDMLUpdatingStatement sqlDMLUpdatingStatement, DMLUpdatingEvaluatorParameter evaluatorParameter) throws EvaluateException;

    int prepareStatement(BaseSQLStatement sqlStatement, ISQLString sqlString);

    <E extends Exception> long executePreparedStatement(int preparedStatementId, PreparedStatementParameters preparedStatementParameters,
            DMLUpdatingPreparedEvaluatorParameter evaluatorParameter, ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E;

    long createPreparedStatementLargeObjectPart(int preparedStatementId, long length) throws IOException;
    void storePreparedStatementLargeObjectPart(int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer, int offset, int length) throws IOException;

    void freePreparedStatement(int preparedStatementId);

    void close();
}
