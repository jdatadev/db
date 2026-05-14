package dev.jdata.db.server;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.parse.ParserException;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.engine.database.ExecuteException;
import dev.jdata.db.engine.database.IDatabases;
import dev.jdata.db.engine.database.operations.IDatabaseExecuteOperations.ISelectResultWriter;
import dev.jdata.db.engine.server.DatabaseServer;
import dev.jdata.db.engine.server.SQLDatabaseServer;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.test.unit.BaseDatabasesTest;
import dev.jdata.db.utils.checks.Checks;

public final class SQLDatabaseServerTest extends BaseDatabasesTest {

    @Test
    @Category(UnitTest.class)
    public void testCRUD() throws ParserException, ExecuteException, IOException {

        final SQLParserFactory parserFactory = ANSISQLParserFactory.INSTANCE;

        final IDatabases databases = createDatabases();

        final DatabaseServer databaseServer = new DatabaseServer(databases);

        final SQLDatabaseServer sqlDatabaseServer = new SQLDatabaseServer(databaseServer, parserFactory);

        final int databaseId = sqlDatabaseServer.createDatabase("testdb", makeDatabaseParameters());

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

        final void executeSQL(String sql) throws ParserException, ExecuteException, IOException {

            final CharBuffer charBuffer = CharBuffer.wrap(sql);

            final ExecuteSQLResultWriter<RuntimeException> resultWriter = new ExecuteSQLResultWriter<RuntimeException>() {

                @Override
                public ISelectResultWriter<RuntimeException> getSelectResultWriter() {

                    throw new UnsupportedOperationException();
                }
            };

            databaseServer.executeSQL(databaseId, sessionId, charBuffer, resultWriter);
        }
    }
}
