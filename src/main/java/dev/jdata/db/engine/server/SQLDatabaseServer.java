package dev.jdata.db.engine.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.ParserException;

import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.EvaluateException;
import dev.jdata.db.engine.database.ExecuteException;
import dev.jdata.db.engine.database.IDatabaseExecuteOperations.SelectResultWriter;
import dev.jdata.db.engine.database.IDatabaseExecutePreparedStatement;
import dev.jdata.db.engine.database.IDatabaseFreePreparedStatement;
import dev.jdata.db.engine.database.IDatabaseLookup;
import dev.jdata.db.engine.database.IDatabaseOperations;
import dev.jdata.db.engine.database.IDatabaseSessions;
import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParser.SQLString;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.allocators.ObjectCache;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class SQLDatabaseServer implements IDatabaseLookup, IDatabaseSessions, IDatabaseExecutePreparedStatement, IDatabaseFreePreparedStatement {

    public interface ExecuteSQLResultWriter<E extends Exception> {

        SelectResultWriter<E> getSelectResultWriter();
    }

    private static final class ExecuteSQLParameter extends ObjectCacheNode {

        private IDatabaseServer server;
        private ExecuteSQLResultWriter<?> resultWriter;

        public void initialize(IDatabaseServer server, ExecuteSQLResultWriter<?> resultWriter) {

            this.server = Objects.requireNonNull(server);
            this.resultWriter = Objects.requireNonNull(resultWriter);
        }
    }

    private final IDatabaseServer server;
    private final SQLParser parser;

    private final NodeObjectCache<SQLAllocatorImpl> allocatorsCache;
    private final NodeObjectCache<SQLScratchExpressionValues> scratchExpressionValuesCache;
    private final ObjectCache<CharBufferLoadStream> charBufferLoadStreamCache;
    private final NodeObjectCache<ExecuteSQLParameter> executeSQLParameterCache;

    public SQLDatabaseServer(IDatabaseServer server, SQLParserFactory parserFactory) {

        Objects.requireNonNull(server);
        Objects.requireNonNull(parserFactory);

        this.server = server;
        this.parser = parserFactory.createParser();

        this.allocatorsCache = new NodeObjectCache<>(SQLAllocatorImpl::new);
        this.scratchExpressionValuesCache = new NodeObjectCache<>(SQLScratchExpressionValues::new);
        this.charBufferLoadStreamCache = new ObjectCache<>(CharBufferLoadStream::new, CharBufferLoadStream[]::new);
        this.executeSQLParameterCache = new NodeObjectCache<>(ExecuteSQLParameter::new);
    }

    @Override
    public int getDatabase(CharSequence dbName) {

        return server.getDatabase(dbName);
    }

    @Override
    public int createDatabase(CharSequence dbName, DatabaseParameters parameters) {

        return server.createDatabase(dbName, parameters);
    }

    @Override
    public int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters) {

        return server.getOrCreateDatabase(dbName, parameters);
    }

    @Override
    public IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId) {

        return server.getDatabaseSessionStatus(databaseId);
    }

    @Override
    public void dropDatabase(int databaseId) {

        Checks.isDatabaseId(databaseId);

        server.dropDatabase(databaseId);
    }

    @Override
    public int addSession(int databaseId, Charset charset) {

        Checks.isDatabaseId(databaseId);
        Objects.requireNonNull(charset);

        return server.addSession(databaseId, charset);
    }

    @Override
    public void closeSession(int databaseId, int sessionId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        server.closeSession(databaseId, sessionId);
    }

    @Override
    public Charset getSessionCharset(int databaseId, int sessionId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        return server.getSessionCharset(databaseId, sessionId);
    }

    public <E extends Exception> long executeSQL(int databaseId, int sessionId, CharBuffer charBuffer, ExecuteSQLResultWriter<E> resultWriter)
            throws ParserException, ExecuteException, E {

        final long result;

        final ExecuteSQLParameter executeSQLParameter;

        synchronized (this) {

            executeSQLParameter = executeSQLParameterCache.allocate();
        }

        try {
            executeSQLParameter.initialize(server, resultWriter);

            result = onSQL(executeSQLParameter, databaseId, sessionId, charBuffer, (instance, dId, sId, sqlStatements, sqlStrings) -> {

                Checks.isExactlyOne(sqlStatements.getNumElements());

                final BaseSQLStatement sqlStatement = sqlStatements.get(0);

                return executeSQL(databaseId, sessionId, sqlStatement, executeSQLParameter);
            });
        }
        finally {

            synchronized (this) {

                executeSQLParameterCache.free(executeSQLParameter);
            }
        }

        return result;
    }

    private static <E extends ExecuteException> long executeSQL(int databaseId, int sessionId, BaseSQLStatement sqlStatement, ExecuteSQLParameter executeSQLParameter)
            throws ExecuteException, E {

        final long result;

        final IDatabaseOperations databaseOperations = executeSQLParameter.server.getDatabaseOperations(databaseId);

        if (sqlStatement instanceof SQLSelectStatement) {

            final SQLSelectStatement sqlSelectStatement = (SQLSelectStatement)sqlStatement;

            @SuppressWarnings("unchecked")
            final SelectResultWriter<E> selectResultWriter = (SelectResultWriter<E>)executeSQLParameter.resultWriter;

            databaseOperations.executeDMLSelectSQL(sessionId, sqlSelectStatement, selectResultWriter);

            result = -1L;
        }
        else if (sqlStatement instanceof SQLDMLUpdatingStatement) {

            final SQLDMLUpdatingStatement sqlDMLUpdatingStatement = (SQLDMLUpdatingStatement)sqlStatement;

            result = databaseOperations.executeDMLUpdateSQL(sessionId, sqlDMLUpdatingStatement);
        }
        else if (sqlStatement instanceof BaseSQLDDLOperationStatement) {

            final BaseSQLDDLOperationStatement sqlDDLStatement = (BaseSQLDDLOperationStatement)sqlStatement;

            databaseOperations.executeDDLSQL(sessionId, sqlDDLStatement);

            result = -1L;
        }
        else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    public int prepareStatement(int databaseId, int sessionId, CharBuffer charBuffer) throws ParserException {

        final long preparedStatementId = onSQL(this.server, databaseId, sessionId, charBuffer, (instance, dId, sId, sqlStatements, sqlStrings) -> {

            Checks.isExactlyOne(sqlStatements.getNumElements());

            return instance.prepareStatement(dId, sId, sqlStatements.get(0), sqlStrings.get(0));
        });

        return Integers.checkUnsignedLongToUnsignedInt(preparedStatementId);
    }

    @Override
    public <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId, PreparedStatementParameters preparedStatementParameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        return server.executePreparedStatement(databaseId, sessionId, preparedStatementId, preparedStatementParameters, resultWriter);
    }

    @Override
    public long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length) throws IOException {

        return server.createPreparedStatementLargeObject(databaseId, sessionId, preparedStatementId, length);
    }

    @Override
    public void storePreparedStatementLargeObjectPart(int databaseId, int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer,
            int offset, int length) throws IOException {

        server.storePreparedStatementLargeObjectPart(databaseId, sessionId, preparedStatementId, largeObjectRef, isFinal, byteBuffer, offset, length);
    }

    @Override
    public void freePreparedStatement(int databaseId, int sessionId, int preparedStatementId) {

        server.freePreparedStatement(databaseId, sessionId, preparedStatementId);
    }

    @FunctionalInterface
    private interface OnParsedStatements<T, E extends Exception> {

        long apply(T instance, int databaseId, int sessionId, IListGetters<BaseSQLStatement> sqlStatements, IListGetters<SQLString> sqlStrings) throws E;
    }

    private <T, R, E extends Exception> long onSQL(T instance, int databaseId, int sessionId, CharBuffer charBuffer, OnParsedStatements<T, E> onParsedStatements)
            throws ParserException, E {

        final SQLAllocatorImpl allocator;
        final SQLScratchExpressionValues scratchExpressionValues;
        final CharBufferLoadStream charBufferLoadStream;

        synchronized (this) {

            allocator = allocatorsCache.allocate();
            scratchExpressionValues = scratchExpressionValuesCache.allocate();
            charBufferLoadStream = charBufferLoadStreamCache.allocate();
        }

        final long result;

        final int initialCapacity = 1;

        final IAddableList<BaseSQLStatement> sqlStatements = allocator.allocateList(initialCapacity);
        final IAddableList<SQLString> sqlStrings = allocator.allocateList(initialCapacity);

        try {
            charBufferLoadStream.initialize(charBuffer);

            try {
                parser.parse(charBufferLoadStream, allocator, scratchExpressionValues, sqlStatements, sqlStrings);
            }
            catch (IOException ex) {

                throw new IllegalStateException(ex);
            }

            result = onParsedStatements.apply(instance, databaseId, sessionId, sqlStatements, sqlStrings);
        }
        finally {

            allocator.freeList(sqlStatements);

            synchronized (this) {

                allocatorsCache.free(allocator);
                scratchExpressionValuesCache.free(scratchExpressionValues);
                charBufferLoadStreamCache.free(charBufferLoadStream);
            }
        }

        return result;
    }
}
