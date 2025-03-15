package dev.jdata.db.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.parse.ParserException;

import dev.jdata.db.DBConstants;
import dev.jdata.db.common.storagebits.BaseMaxNumStorageBitsAdapter;
import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.Databases;
import dev.jdata.db.engine.database.DatabasesAllocators;
import dev.jdata.db.engine.database.ExecuteException;
import dev.jdata.db.engine.database.IDatabaseExecuteOperations.SelectResultWriter;
import dev.jdata.db.engine.database.IStringCache;
import dev.jdata.db.engine.database.StringCache;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.server.DatabaseServer;
import dev.jdata.db.engine.server.SQLDatabaseServer;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.DBSession.LargeObjectStorer;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.engine.transactions.Transactions.TransactionFactory;
import dev.jdata.db.engine.transactions.mvcc.MVCCTransaction;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.DatabaseSchemas;
import dev.jdata.db.schema.types.SchemaCustomType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.checks.Checks;

public class SQLDatabaseServerTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testCRUD() throws ParserException, ExecuteException {

        final SQLParserFactory parserFactory = new ANSISQLParserFactory();

        final StringCache stringCache = new StringCache();
        final boolean cacheStatements = false;

        final Databases databases = new Databases(stringCache, cacheStatements);
        final DatabaseServer databaseServer = new DatabaseServer(databases);

        final SQLDatabaseServer sqlDatabaseServer = new SQLDatabaseServer(databaseServer, parserFactory);

        final DatabaseParameters databaseParameters = makeDatabaseParameters();

        final int databaseId = sqlDatabaseServer.createDatabase("testdb", databaseParameters);

        try {
            final int sessionId = sqlDatabaseServer.addSession(databaseId, Charset.defaultCharset());

            final TestSession session = new TestSession(sqlDatabaseServer, databaseId, sessionId);

            try {
                final String createTableSQL = "create table test_table (test_column integer)";

                session.executeSQL(createTableSQL);
            }
            finally {

                sqlDatabaseServer.closeSession(databaseId, sessionId);
            }
        }
        finally {

            sqlDatabaseServer.dropDatabase(databaseId);
        }
    }

    private static final class TestSession {

        private final SQLDatabaseServer databaseServer;
        private final int databaseId;
        private final int sessionId;

        TestSession(SQLDatabaseServer databaseServer, int databaseId, int sessionId) {

            this.databaseServer = Objects.requireNonNull(databaseServer);
            this.databaseId = Checks.isDatabaseId(databaseId);
            this.sessionId = Checks.isSessionDescriptor(sessionId);
        }

        final void executeSQL(String sql) throws ParserException, ExecuteException {

            final CharBuffer charBuffer = CharBuffer.wrap(sql);

            final ExecuteSQLResultWriter<RuntimeException> resultWriter = new ExecuteSQLResultWriter<RuntimeException>() {

                @Override
                public SelectResultWriter<RuntimeException> getSelectResultWriter() {

                    throw new UnsupportedOperationException();
                }
            };

            databaseServer.executeSQL(databaseId, sessionId, charBuffer, resultWriter);
        }
    }

    private static DatabaseParameters makeDatabaseParameters() {

        final DatabaseParameters parameters = new DatabaseParameters();

        final NumStorageBitsGetter numStorageBitsGetter = makeNumStorageBitsGetter();
        final DatabasesAllocators databasesAllocators = new DatabasesAllocators(numStorageBitsGetter);

        final IStringCache stringCache = new StringCache();
        final CharacterBuffersAllocator characterBuffersAllocator = new CharacterBuffersAllocator();
        final StringManagement stringManagement = new StringManagement(stringCache, characterBuffersAllocator);
        final LargeObjectStorer<IOException> largeObjectStorer = makeLargeObjectStorer();

        final TransactionFactory transactionFactory = makeTransactionFactory();

        parameters.initializeStatic(databasesAllocators, stringManagement, largeObjectStorer, transactionFactory);

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        final String databaseName = "testdb";

        final DatabaseSchema databaseSchema = DatabaseSchema.empty(databaseName, schemaVersion);
        final DatabaseSchemas databaseSchemas = DatabaseSchemas.of(databaseName, databaseSchema);

        parameters.initializePerDatabase(databaseSchemas, null, DBConstants.NO_TRANSACTION_ID, null);

        return parameters;
    }

    private static NumStorageBitsGetter makeNumStorageBitsGetter() {

        final BaseMaxNumStorageBitsAdapter maxNumStorageBitsAdapter = new BaseMaxNumStorageBitsAdapter() {

            @Override
            public Integer onCustomType(SchemaCustomType schemaDataType, NumStorageBitsParameters parameter) {

                throw new UnsupportedOperationException();
            }
        };

        return new NumStorageBitsGetter() {

            @Override
            public int getMinNumBits(SchemaDataType schemaDataType) {

                throw new UnsupportedOperationException();
            }

            @Override
            public int getMaxNumBits(SchemaDataType schemaDataType) {

                return schemaDataType.visit(maxNumStorageBitsAdapter, null);
            }
        };
    }

    private static LargeObjectStorer<IOException> makeLargeObjectStorer() {

        return new LargeObjectStorer<IOException>() {

            @Override
            public long createLargeObject(long length) throws IOException {

                throw new UnsupportedOperationException();
            }

            @Override
            public void addLargeObjectPart(long largeObjectRef, ByteBuffer byteBuffer, int offset, int length) throws IOException {

                throw new UnsupportedOperationException();
            }

            @Override
            public void closeLargeObject(long largeObjectRef) throws IOException {

                throw new UnsupportedOperationException();
            }
        };
    }

    private static TransactionFactory makeTransactionFactory() {

        return () -> new Transaction(new MVCCTransaction());
    }

    private static final class ANSISQLParser extends SQLParser {

        ANSISQLParser(SQLToken[] statementTokens, SQLToken[] createOrDropTokens, SQLToken[] alterTokens, SQLParserFactory parserFactory) {
            super(statementTokens, createOrDropTokens, alterTokens, parserFactory);
        }
    }

    private static final class ANSISQLParserFactory extends SQLParserFactory {

        @Override
        public SQLParser createParser() {

            return new ANSISQLParser(getSQLstatementTokens(), getSQLCreateOrDropTokens(), getSQLAlterTokens(), this);
        }
    }
}
