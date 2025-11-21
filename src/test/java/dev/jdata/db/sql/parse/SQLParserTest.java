package dev.jdata.db.sql.parse;

import org.junit.Test;
import org.jutils.ast.objects.list.IImmutableIndexList;
import org.jutils.io.loadstream.StringLoadStream;
import org.jutils.parse.ParserException;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.test.unit.BaseSQLTest;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

public final class SQLParserTest extends BaseSQLTest {

    private static final String CREATE_TABLE_SQL = "create table test_table (test_column integer)";

    @Test
    public void testParseStatement() throws ParserException {

        final SQLCreateTableStatement sqlCreateTableStatement = checkParseStatement(CREATE_TABLE_SQL, SQLCreateTableStatement.class);

        assertThat(sqlCreateTableStatement).isNotNull();
    }

    @Test
    public void testParseMultipleStatements() throws ParserException {

        final int numStatements = 3;

        final String sql = Strings.repeat(CREATE_TABLE_SQL, numStatements, ';');

        checkParseStatements(sql, numStatements, SQLCreateTableStatement.class);
    }

    @SuppressWarnings("unchecked")
    private static <T extends BaseSQLStatement> T checkParseStatement(String string, Class<T> expectedSQLStatementClass) throws ParserException {

        final IImmutableIndexList<BaseSQLStatement> sqlStatements = parseStatements(string);

        assertThat(sqlStatements.getNumElements()).isEqualTo(1L);

        final T result = (T)sqlStatements.getHead();

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(expectedSQLStatementClass);

        return result;
    }

    private static IImmutableIndexList<BaseSQLStatement> checkParseStatements(String string, long expectedNumStatements, Class<?> expectedSQLStatementClass)
            throws ParserException {

        final IImmutableIndexList<BaseSQLStatement> sqlStatements = parseStatements(string);

        assertThat(sqlStatements.getNumElements()).isEqualTo(expectedNumStatements);

        for (int i = 0; i < expectedNumStatements; ++ i) {

            final BaseSQLStatement sqlStatement = sqlStatements.get(i);

            assertThat(sqlStatement).isNotNull();
            assertThat(sqlStatement).isInstanceOf(SQLCreateTableStatement.class);
        }

        return sqlStatements;
    }

    private static IImmutableIndexList<BaseSQLStatement> parseStatements(String string) throws ParserException {

        final SQLParser sqlParser = ANSISQLParserFactory.INSTANCE.createParser();

        final StringLoadStream loadStream = new StringLoadStream(string);

        return sqlParser.parse(loadStream, RuntimeException::new, createSQLAllocator(), new SQLScratchExpressionValues(AllocationType.HEAP));
    }
}
