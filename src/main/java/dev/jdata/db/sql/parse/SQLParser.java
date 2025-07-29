package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;

import org.jutils.ast.objects.list.IAddable;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IImmutableIndexList;
import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.buffers.LoadStreamStringBuffers;
import org.jutils.io.loadstream.LoadStream;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.ddl.index.SQLCreateIndexParser;
import dev.jdata.db.sql.parse.ddl.index.SQLDropIndexParser;
import dev.jdata.db.sql.parse.ddl.table.SQLAlterTableParser;
import dev.jdata.db.sql.parse.ddl.table.SQLColumnDefinitionParser;
import dev.jdata.db.sql.parse.ddl.table.SQLCreateTableParser;
import dev.jdata.db.sql.parse.ddl.table.SQLDropTableParser;
import dev.jdata.db.sql.parse.dml.delete.SQLDeleteParser;
import dev.jdata.db.sql.parse.dml.insert.SQLInsertParser;
import dev.jdata.db.sql.parse.dml.select.SQLSelectParser;
import dev.jdata.db.sql.parse.dml.update.SQLUpdateParser;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.sql.parse.expression.SQLSubSelectParser;
import dev.jdata.db.sql.parse.trigger.SQLCreateTriggerParser;
import dev.jdata.db.sql.parse.trigger.SQLDropTriggerParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.checks.Checks;

public abstract class SQLParser extends BaseSQLParser {

    private final SQLToken[] statementTokens;
    private final SQLToken[] createOrDropTokens;
    private final SQLToken[] alterTokens;

    private final SQLCreateTableParser createTableParser;
    private final SQLAlterTableParser alterTableParser;
    private final SQLDropTableParser dropTableParser;

    private final SQLCreateIndexParser createIndexParser;
    private final SQLDropIndexParser dropIndexParser;

    private final SQLSelectParser selectParser;
    private final SQLInsertParser insertParser;
    private final SQLUpdateParser updateParser;
    private final SQLDeleteParser deleteParser;

    private final SQLCreateTriggerParser createTriggerParser;
    private final SQLDropTriggerParser dropTriggerParser;

    private final NodeObjectCache<SQLExpressionLexer<?, ?>> sqlExpressionLexerCache;

    public SQLParser(SQLToken[] statementTokens, SQLToken[] createOrDropTokens, SQLToken[] alterTokens, SQLParserFactory parserFactory) {

        this.statementTokens = Checks.isNotEmpty(statementTokens);
        this.createOrDropTokens = Checks.isNotEmpty(createOrDropTokens);
        this.alterTokens = Checks.isNotEmpty(alterTokens);

        final SQLExpressionParser expressionParser = parserFactory.createExpressionParser();
        final SQLConditionParser conditionParser = parserFactory.createConditionParser(expressionParser);

        final SQLWhereClauseParser whereClauseParser = parserFactory.createWhereClauseParser(conditionParser);

        final SQLSubSelectParser subSelectParser = parserFactory.createSubSelectParser(expressionParser, conditionParser, whereClauseParser);

        expressionParser.initialize(subSelectParser);

        final SQLColumnDefinitionParser columnDefinitionParser = parserFactory.createColumnDefinitionParser(expressionParser);

        this.createTableParser = parserFactory.createCreateTableParser(columnDefinitionParser);
        this.alterTableParser = parserFactory.createAlterTableParser(columnDefinitionParser);
        this.dropTableParser = parserFactory.createDropTableParser();

        this.createIndexParser = parserFactory.createCreateIndexParser();
        this.dropIndexParser = parserFactory.createDropIndexParser();

        this.selectParser = parserFactory.createSelectParser(expressionParser, conditionParser, whereClauseParser);

        this.insertParser = parserFactory.createInsertParser(expressionParser);
        this.updateParser = parserFactory.createUpdateParser(expressionParser, whereClauseParser);
        this.deleteParser = parserFactory.createDeleteParser(whereClauseParser);

        this.createTriggerParser = parserFactory.createCreateTriggerParser(conditionParser, insertParser, updateParser, deleteParser);
        this.dropTriggerParser = parserFactory.createDropTriggerParser();

        this.sqlExpressionLexerCache = new NodeObjectCache<>(SQLExpressionLexer::new);
    }

    final <E extends Exception> IImmutableIndexList<BaseSQLStatement> parse(LoadStream<E> loadStream, Function<String, E> createEOFException, ISQLAllocator allocator,
            SQLScratchExpressionValues scratchExpressionValues) throws ParserException, E {

        final IImmutableIndexList<BaseSQLStatement> result;

        final IAddableList<BaseSQLStatement> sqlStatements = allocator.allocateList(1);

        try {
            parse(loadStream, createEOFException, allocator, scratchExpressionValues, sqlStatements, null);
        }
        finally {

            result = sqlStatements.toImmutableIndexList();

            allocator.freeList(sqlStatements);
        }

        return result;
    }

    private <E extends Exception> void parse(LoadStream<E> loadStream, Function<String, E> createEOFException, ISQLAllocator allocator,
            SQLScratchExpressionValues scratchExpressionValues, IAddable<BaseSQLStatement> sqlStatementDst, IAddable<SQLString> sqlStringsDst) throws ParserException, E {

        final LoadStreamStringBuffers<E> buffer = new LoadStreamStringBuffers<>(loadStream);

        parse(buffer, createEOFException, allocator, scratchExpressionValues, sqlStatementDst, sqlStringsDst);
    }

    public <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(BUFFER buffer, Function<String, E> createEOFException, ISQLAllocator allocator,
            SQLScratchExpressionValues scratchExpressionValues, IAddable<BaseSQLStatement> sqlStatementDst, IAddable<SQLString> sqlStringsDst) throws ParserException, E {

        Objects.requireNonNull(buffer);
        Objects.requireNonNull(createEOFException);
        Objects.requireNonNull(scratchExpressionValues);
        Objects.requireNonNull(sqlStatementDst);

        @SuppressWarnings("unchecked")
        final SQLExpressionLexer<E, BUFFER> lexer = (SQLExpressionLexer<E, BUFFER>)sqlExpressionLexerCache.allocate();

        try {
            lexer.initialize(buffer, createEOFException, allocator, buffer, scratchExpressionValues);

            if (!skipEmptyStatements(lexer)) {

                for (;;) {

                    final BaseSQLStatement sqlStatement = parseStatement(lexer);

                    sqlStatementDst.add(sqlStatement);

                    if (skipEmptyStatements(lexer)) {

                        break;
                    }
                }
            }
        }
        finally {

            sqlExpressionLexerCache.free(lexer);
        }
    }

    private static final SQLToken[] SEMI_COLON_OR_EOF = new SQLToken[] {

            SQLToken.SEMI_COLON,
            SQLToken.EOF
    };

    private <E extends Exception, I extends CharInput<E>> boolean skipEmptyStatements(SQLLexer<E, I> lexer) throws ParserException, E {

        boolean isEOF = false;

        for (;;) {

            final SQLToken token = lexer.peek(SEMI_COLON_OR_EOF);

            if (token == SQLToken.SEMI_COLON) {

                lexer.lexSkip(SQLToken.SEMI_COLON);
            }
            else {
                if (token == SQLToken.EOF) {

                    isEOF = true;
                }

                break;
            }
        }

        return isEOF;
    }

    private <E extends Exception, I extends CharInput<E>> BaseSQLStatement parseStatement(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        final BaseSQLStatement result;

        final SQLToken statementToken = lexer.lex(statementTokens);

        if (statementToken != SQLToken.NONE) {

            final long statementKeyword = lexer.getStringRef();

            result = processStatement(lexer, statementToken, statementKeyword);
        }
        else {
            result = null;
        }

        return result;
    }

    <E extends Exception, I extends CharInput<E>> BaseSQLStatement processStatement(SQLExpressionLexer<E, I> lexer, SQLToken statementToken, long statementKeyword)
            throws ParserException, E {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(statementToken);
        checkIsKeyword(statementKeyword);

        final BaseSQLStatement sqlStatement;

        switch (statementToken) {

        case CREATE:

            final long createKeyword = statementKeyword;

            switch (lexer.lex(createOrDropTokens)) {

            case TABLE:

                final long tableKeyword = lexer.getStringRef();

                sqlStatement = createTableParser.parseCreateTable(lexer, createKeyword, tableKeyword);
                break;

            case INDEX:

                final long indexKeyword = lexer.getStringRef();

                sqlStatement = createIndexParser.parseCreateIndex(lexer, createKeyword, indexKeyword);
                break;

            case TRIGGER:

                final long triggerKeyword = lexer.getStringRef();

                sqlStatement = createTriggerParser.parseCreateTrigger(lexer, createKeyword, triggerKeyword);
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case ALTER:

            final long alterKeyword = statementKeyword;

            switch (lexer.lex(alterTokens)) {

            case TABLE:

                final long tableKeyword = lexer.getStringRef();

                sqlStatement = alterTableParser.parseAlterTable(lexer, alterKeyword, tableKeyword);
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DROP:

            final long dropKeyword = statementKeyword;

            switch (lexer.lex(createOrDropTokens)) {

            case TABLE:

                final long tableKeyword = lexer.getStringRef();

                sqlStatement = dropTableParser.parseDropTable(lexer, dropKeyword, tableKeyword);
                break;

            case INDEX:

                final long indexKeyword = lexer.getStringRef();

                sqlStatement = dropIndexParser.parseDropIndex(lexer, dropKeyword, indexKeyword);
                break;

            case TRIGGER:

                final long triggerKeyword = lexer.getStringRef();

                sqlStatement = dropTriggerParser.parseDropTrigger(lexer, dropKeyword, triggerKeyword);
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case SELECT:

            final long selectKeyword = lexer.getStringRef();

            sqlStatement = selectParser.parseSelect(lexer, selectKeyword);
            break;

        case INSERT:

            final long insertKeyword = lexer.getStringRef();

            sqlStatement = insertParser.parseInsert(lexer, insertKeyword);
            break;

        case UPDATE:

            final long updateKeyword = lexer.getStringRef();

            sqlStatement = updateParser.parseUpdate(lexer, updateKeyword);
            break;

        case DELETE:

            final long deleteKeyword = lexer.getStringRef();

            sqlStatement = deleteParser.parseDelete(lexer, deleteKeyword);
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return sqlStatement;
    }
}
