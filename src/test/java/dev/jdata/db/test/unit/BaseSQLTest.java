package dev.jdata.db.test.unit;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Objects;

import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.buffers.StringBuffers;
import org.jutils.io.loadstream.StringLoadStream;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.engine.server.SQLAllocator;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserHelper;
import dev.jdata.db.sql.strings.ISQLString;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapLongIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.ILongIndexList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseSQLTest extends BaseTest {

    protected static abstract class BaseParsed {

        private final StringResolver stringResolver;

        private BaseParsed(StringResolver stringResolver) {

            this.stringResolver = Objects.requireNonNull(stringResolver);
        }

        public final StringResolver getStringResolver() {
            return stringResolver;
        }
    }

    protected static final class SingleParsedStatement<T extends BaseSQLStatement> extends BaseParsed {

        private final T statement;
        private final ISQLString sqlString;

        private SingleParsedStatement(T statement, StringResolver stringResolver, ISQLString sqlString) {
            super(stringResolver);

            this.statement = Objects.requireNonNull(statement);
            this.sqlString = Objects.requireNonNull(sqlString);
        }

        public T getStatement() {
            return statement;
        }

        public ISQLString getSQLString() {
            return sqlString;
        }
    }

    protected static final class ParsedStatement {

        private final BaseSQLStatement statement;
        private final ISQLString sqlString;

        private ParsedStatement(BaseSQLStatement statement, ISQLString sqlString) {

            this.statement = Objects.requireNonNull(statement);
            this.sqlString = Objects.requireNonNull(sqlString);
        }

        public BaseSQLStatement getStatement() {
            return statement;
        }

        public ISQLString getSQLString() {
            return sqlString;
        }
    }

    protected static final class ParsedStatements extends BaseParsed {

        private final IIndexList<ParsedStatement> statements;

        private ParsedStatements(IIndexList<ParsedStatement> statements, StringResolver stringResolver) {
            super(stringResolver);

            this.statements = Objects.requireNonNull(statements);
        }

        public IIndexList<ParsedStatement> getStatements() {
            return statements;
        }
    }

    protected static ParsedStatements checkParseANSIStatements(String string, IIndexList<? extends Class<? extends BaseSQLStatement>> sqlStatementClasses)
            throws ParserException {

        Objects.requireNonNull(string);

        final ParsedStatements parsedStatements = parseANSIStatements(string);

        final IIndexList<ParsedStatement> sqlStatements = parsedStatements.statements;
        final long numSQLStatements = sqlStatements.getNumElements();

        assertThat(numSQLStatements).isEqualTo(sqlStatementClasses.getNumElements());

        for (int i = 0; i < numSQLStatements; ++ i) {

            final ParsedStatement parsedStatement = sqlStatements.get(i);

            assertThat(parsedStatement.statement).isInstanceOf(sqlStatementClasses.get(i));
        }

        return parsedStatements;
    }

    protected static <T extends BaseSQLStatement> SingleParsedStatement<T> checkParseANSIStatement(String string, Class<T> sqlStatementClass) throws ParserException {

        Objects.requireNonNull(string);
        Objects.requireNonNull(sqlStatementClass);

        final ParsedStatements parsedStatements = parseANSIStatements(string);

        final IIndexList<ParsedStatement> parsedStatementsList = parsedStatements.statements;

        assertThat(parsedStatementsList.getNumElements()).isEqualTo(1L);

        final ParsedStatement parsedStatement = parsedStatementsList.getHead();

        @SuppressWarnings("unchecked")
        final T sqlStatement = (T)parsedStatement.statement;

        assertThat(sqlStatement).isNotNull();
        assertThat(sqlStatement).isInstanceOf(sqlStatementClass);

        return new SingleParsedStatement<>(sqlStatement, parsedStatements.getStringResolver(), parsedStatement.sqlString);
    }

    private static ParsedStatements parseANSIStatements(String string) throws ParserException {

        final StringLoadStream loadStream = new StringLoadStream(string);
        final StringBuffers stringBuffers = new StringBuffers(loadStream);

        final IIndexList<ParsedStatement> parsedStatement = parseANSIStatements(stringBuffers);

        return new ParsedStatements(parsedStatement, stringBuffers);
    }

    private static <T extends BaseStringBuffers<RuntimeException>> IIndexList<ParsedStatement> parseANSIStatements(T buffers) throws ParserException {

        Objects.requireNonNull(buffers);

        final SQLParser sqlParser = ANSISQLParserFactory.INSTANCE.createParser();

        final SQLParserHelper<IHeapIndexList<BaseSQLStatement>, IHeapIndexListBuilder<BaseSQLStatement>, IHeapIndexListAllocator<BaseSQLStatement>> sqlParserHelper
                = new SQLParserHelper<>(sqlParser, IHeapIndexListAllocator::create);

        final IHeapIndexListBuilder<BaseSQLStatement> sqlStatementsBuilder = IHeapIndexListBuilder.create(BaseSQLStatement[]::new);
        final IHeapLongIndexListBuilder sqlStringsBuilder = IHeapLongIndexListBuilder.create();

        sqlParserHelper.parse(buffers, createSQLAllocator(), sqlStatementsBuilder, sqlStringsBuilder, RuntimeException::new);

        final IIndexList<BaseSQLStatement> sqlStatements = sqlStatementsBuilder.buildOrEmpty();
        final ILongIndexList sqlStrings = sqlStringsBuilder.buildOrEmpty();

        assertThat(sqlStatements).isSameNumElements(sqlStrings);

        final long numSQLStatements = sqlStatements.getNumElements();

        final IHeapIndexListBuilder<ParsedStatement> parsedStatementsBuilder = IHeapIndexListBuilder.create(ParsedStatement[]::new);

        for (int i = 0; i < numSQLStatements; ++ i) {

            final String sqlString = buffers.asString(sqlStrings.get(i));

            parsedStatementsBuilder.addTail(new ParsedStatement(sqlStatements.get(i), new TestSQLString(sqlString)));
        }

        return parsedStatementsBuilder.buildOrEmpty();
    }

    private static class TestSQLString implements ISQLString {

        private String string;

        TestSQLString(String string) {

            this.string = Objects.requireNonNull(string);
        }

        @Override
        public long writeToCharBuffer(CharBuffer dst, long offset) {

            final long result;

            if (string == null) {

                result = EOF;
            }
            else {
                final int length = string.length();

                Checks.checkLongIndex(offset, length);

                dst.append(string, Integers.checkUnsignedLongToUnsignedInt(offset), length);

                this.string = null;

                result = length;
            }

            return result;
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {

            dataOutput.writeUTF(string);
        }

        @Override
        public long getLength() {

            return string.length();
        }

        @Override
        public void append(Appendable appendable) throws IOException {

            appendable.append(string);
        }
    }

    protected static ISQLAllocator createSQLAllocator() {

        return new SQLAllocator(AllocationType.HEAP);
    }

    private static ISQLString createSQLString(String string) {

        Objects.requireNonNull(string);

        return new TestSQLString(string);
    }
}
