package dev.jdata.db.sql.parse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.io.buffers.StringBuffers;
import org.jutils.io.loadstream.LoadStream;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
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
    }

    public List<BaseSQLStatement> parse(LoadStream loadStream, SQLAllocator allocator, SQLScratchExpressionValues scratchExpressionValues) throws ParserException, IOException {

        Objects.requireNonNull(loadStream);

        final StringBuffers buffer = new StringBuffers(loadStream);

        final SQLExpressionLexer lexer = new SQLExpressionLexer(buffer, allocator, buffer, scratchExpressionValues);

        final List<BaseSQLStatement> result = allocator.allocateList(1);

        if (!skipEmptyStatements(lexer)) {

            for (;;) {

                final BaseSQLStatement sqlStatement = parseStatement(lexer);

                result.add(sqlStatement);

                if (skipEmptyStatements(lexer)) {

                    break;
                }
            }
        }

        return result;
    }

    private static final SQLToken[] SEMI_COLON_OR_EOF = new SQLToken[] {

            SQLToken.SEMI_COLON,
            SQLToken.EOF
    };

    private boolean skipEmptyStatements(SQLLexer lexer) throws ParserException, IOException {

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

    private BaseSQLStatement parseStatement(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLToken statementToken = lexer.lex(statementTokens);

        final long statementKeyword = lexer.getStringRef();

        return processStatement(lexer, statementToken, statementKeyword);
    }

    BaseSQLStatement processStatement(SQLExpressionLexer lexer, SQLToken statementToken, long statementKeyword) throws ParserException, IOException {

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
