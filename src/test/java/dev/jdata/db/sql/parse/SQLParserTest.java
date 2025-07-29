package dev.jdata.db.sql.parse;

import org.junit.Test;
import org.jutils.ast.objects.list.IImmutableIndexList;
import org.jutils.io.loadstream.StringLoadStream;
import org.jutils.parse.ParserException;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.engine.server.SQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.test.unit.BaseTest;

public final class SQLParserTest extends BaseTest {

    @Test
    public void testParse() throws ParserException {

        final SQLCreateTableStatement sqlCreateTableStatement = checkParseStatement("create table test_table (test_column integer)", SQLCreateTableStatement.class);

        assertThat(sqlCreateTableStatement).isNotNull();
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseSQLStatement> T checkParseStatement(String string, Class<T> sqlStatementClass) throws ParserException {

        final IImmutableIndexList<BaseSQLStatement> sqlStatements = checkParseStatements(string);

        assertThat(sqlStatements.getNumElements()).isEqualTo(1L);

        final T result = (T)sqlStatements.getHead();

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(sqlStatementClass);

        return result;
    }

    private IImmutableIndexList<BaseSQLStatement> checkParseStatements(String string) throws ParserException {

        final SQLParser sqlParser = ANSISQLParserFactory.INSTANCE.createParser();

        final StringLoadStream loadStream = new StringLoadStream(string);

        return sqlParser.parse(loadStream, RuntimeException::new, new SQLAllocator(), new SQLScratchExpressionValues());
    }
}
