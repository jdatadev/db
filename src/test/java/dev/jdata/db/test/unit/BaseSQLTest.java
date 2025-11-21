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
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserHelper;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
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

    protected static final class ParsedStatement<T extends BaseSQLStatement> extends BaseParsed {

        private final T statement;

        private ParsedStatement(T statement, StringResolver stringResolver) {
            super(stringResolver);

            this.statement = Objects.requireNonNull(statement);
        }

        public T getStatement() {
            return statement;
        }
    }

    protected static final class ParsedStatements extends BaseParsed {

        private final IIndexList<BaseSQLStatement> statements;

        private ParsedStatements(IIndexList<BaseSQLStatement> statements, StringResolver stringResolver) {
            super(stringResolver);

            this.statements = Objects.requireNonNull(statements);
        }

        public IIndexList<BaseSQLStatement> getStatements() {
            return statements;
        }
    }

    protected static <T extends BaseSQLStatement> T checkParseANSIStatement(String string, Class<T> sqlStatementClass) throws ParserException {

        Objects.requireNonNull(string);
        Objects.requireNonNull(sqlStatementClass);

        return checkParseANSIStatementAll(string, sqlStatementClass).statement;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends BaseSQLStatement> ParsedStatement<T> checkParseANSIStatementAll(String string, Class<T> sqlStatementClass) throws ParserException {

        Objects.requireNonNull(string);
        Objects.requireNonNull(sqlStatementClass);

        final ParsedStatements parsedStatements = parseANSIStatements(string);

        final IIndexList<BaseSQLStatement> sqlStatements = parsedStatements.statements;

        assertThat(sqlStatements.getNumElements()).isEqualTo(1L);

        final T sqlStatement = (T)sqlStatements.getHead();

        assertThat(sqlStatement).isNotNull();
        assertThat(sqlStatement).isInstanceOf(sqlStatementClass);

        return new ParsedStatement<T>(sqlStatement, parsedStatements.getStringResolver());
    }

    private static ParsedStatements parseANSIStatements(String string) throws ParserException {

        final StringLoadStream loadStream = new StringLoadStream(string);
        final StringBuffers stringBuffers = new StringBuffers(loadStream);

        final IIndexList<BaseSQLStatement> parsedStatement = parseANSIStatements(stringBuffers);

        return new ParsedStatements(parsedStatement, stringBuffers);
    }

    private static <T extends BaseStringBuffers<RuntimeException>> IIndexList<BaseSQLStatement> parseANSIStatements(T buffers) throws ParserException {

        Objects.requireNonNull(buffers);

        final SQLParser sqlParser = ANSISQLParserFactory.INSTANCE.createParser();

        final SQLParserHelper<IHeapIndexList<BaseSQLStatement>, IHeapIndexListBuilder<BaseSQLStatement>, IHeapIndexListAllocator<BaseSQLStatement>> sqlParserHelper
                = new SQLParserHelper<>(sqlParser, IHeapIndexListAllocator::create);

        return sqlParserHelper.parse(buffers, createSQLAllocator(), RuntimeException::new);
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

    protected static ISQLString createSQLString(String string) {

        Objects.requireNonNull(string);

        return new TestSQLString(string);
    }
}
