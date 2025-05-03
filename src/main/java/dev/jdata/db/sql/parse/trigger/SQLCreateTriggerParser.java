package dev.jdata.db.sql.parse.trigger;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IAddable;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IImutableList;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLTableName;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.sql.ast.statements.trigger.BaseSQLTrigger;
import dev.jdata.db.sql.ast.statements.trigger.BaseSQLTriggerActionClause;
import dev.jdata.db.sql.ast.statements.trigger.SQLAfterTriggeredActions;
import dev.jdata.db.sql.ast.statements.trigger.SQLBeforeTriggeredActions;
import dev.jdata.db.sql.ast.statements.trigger.SQLCreateTriggerStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLDeleteTrigger;
import dev.jdata.db.sql.ast.statements.trigger.SQLDeleteTriggerEvent;
import dev.jdata.db.sql.ast.statements.trigger.SQLForEachRowTriggeredActions;
import dev.jdata.db.sql.ast.statements.trigger.SQLInsertTrigger;
import dev.jdata.db.sql.ast.statements.trigger.SQLInsertTriggerEvent;
import dev.jdata.db.sql.ast.statements.trigger.SQLNewTriggerCorrelation;
import dev.jdata.db.sql.ast.statements.trigger.SQLOldTriggerCorrelation;
import dev.jdata.db.sql.ast.statements.trigger.SQLSelectTrigger;
import dev.jdata.db.sql.ast.statements.trigger.SQLSelectTriggerEvent;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggerActionClause;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggerInsertActionClause;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggerSelectOrDeleteActionClause;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggerUpdateActionClause;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggeredAction;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggeredStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLUpdateTrigger;
import dev.jdata.db.sql.ast.statements.trigger.SQLUpdateTriggerEvent;
import dev.jdata.db.sql.parse.SQLAllocatorLexer;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.dml.delete.SQLDeleteParser;
import dev.jdata.db.sql.parse.dml.insert.SQLInsertParser;
import dev.jdata.db.sql.parse.dml.update.SQLUpdateParser;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;

public class SQLCreateTriggerParser extends SQLStatementParser {

    private static final SQLToken[] EVENT_DML_STATEMENT_TOKENS = new SQLToken[] {

            SQLToken.SELECT,
            SQLToken.INSERT,
            SQLToken.UPDATE,
            SQLToken.DELETE
    };

    private final SQLConditionParser conditionParser;
    private final SQLInsertParser insertParser;
    private final SQLUpdateParser updateParser;
    private final SQLDeleteParser deleteParser;

    public SQLCreateTriggerParser(SQLConditionParser conditionParser, SQLInsertParser insertParser, SQLUpdateParser updateParser, SQLDeleteParser deleteParser) {

        this.conditionParser = Objects.requireNonNull(conditionParser);
        this.insertParser = Objects.requireNonNull(insertParser);
        this.updateParser = Objects.requireNonNull(updateParser);
        this.deleteParser = Objects.requireNonNull(deleteParser);
    }

    public SQLCreateTriggerStatement parseCreateTrigger(SQLExpressionLexer lexer, long createKeyword, long triggerKeyword) throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        checkIsKeyword(createKeyword);
        checkIsKeyword(triggerKeyword);

        final long triggerName = lexer.lexName();

        final BaseSQLTrigger<?> trigger = parseTrigger(lexer);

        return new SQLCreateTriggerStatement(makeContext(), createKeyword, triggerKeyword, triggerName, trigger);
    }

    private BaseSQLTrigger<?> parseTrigger(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLToken dmlStatementToken = lexer.lex(EVENT_DML_STATEMENT_TOKENS);

        final BaseSQLTrigger<?> result;

        switch (dmlStatementToken) {

        case SELECT:

            final long selectKeyword = lexer.getStringRef();

            result = parseSelectTrigger(lexer, selectKeyword);
            break;

        case INSERT:

            final long insertKeyword = lexer.getStringRef();

            result = parseInsertTrigger(lexer, insertKeyword);
            break;

        case UPDATE:

            final long updateKeyword = lexer.getStringRef();

            result = parseUpdateTrigger(lexer, updateKeyword);
            break;

        case DELETE:

            final long deleteKeyword = lexer.getStringRef();

            result = parseDeleteTrigger(lexer, deleteKeyword);
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    private SQLSelectTrigger parseSelectTrigger(SQLExpressionLexer lexer, long selectKeyword) throws ParserException, IOException {

        final SQLSelectTriggerEvent event = parseSelectTriggerEvent(lexer, selectKeyword);

        final BaseSQLTriggerActionClause actionClause = parseSelectOrDeleteAction(lexer);

        return new SQLSelectTrigger(makeContext(), event, actionClause);
    }

    private static SQLSelectTriggerEvent parseSelectTriggerEvent(SQLAllocatorLexer lexer, long selectKeyword) throws ParserException, IOException {

        final long ofKeyword;
        final SQLColumnNames columnNames;

        if (lexer.peek(SQLToken.OF)) {

            ofKeyword = lexer.lexKeyword(SQLToken.OF);

            columnNames = parseColumnNames(lexer, lexer.getAllocator(), false);
        }
        else {
            ofKeyword = BaseASTElement.NO_KEYWORD;
            columnNames = null;
        }

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLTableName tableName = parseTableName(lexer);

        return new SQLSelectTriggerEvent(makeContext(), selectKeyword, ofKeyword, columnNames, onKeyword, tableName);
    }

    private SQLInsertTrigger parseInsertTrigger(SQLExpressionLexer lexer, long insertKeyword) throws ParserException, IOException {

        final SQLInsertTriggerEvent event = parseInsertTriggerEvent(lexer, insertKeyword);

        final BaseSQLTriggerActionClause actionClause;

        if (lexer.peek(SQLToken.REFERENCING)) {

            final long referencingKeyword = lexer.lexKeyword(SQLToken.REFERENCING);

            final SQLNewTriggerCorrelation newTriggerCorrelation = parseNewTriggerCorrelation(lexer);

            actionClause = new SQLTriggerInsertActionClause(makeContext(), referencingKeyword, newTriggerCorrelation, parseBeforeTriggeredActions(lexer),
                    parseForEachRowTriggeredActions(lexer), parseAfterTriggeredActions(lexer));
        }
        else {
            actionClause = parseActionClause(lexer);
        }

        return new SQLInsertTrigger(makeContext(), event, actionClause);

    }

    private static SQLInsertTriggerEvent parseInsertTriggerEvent(SQLAllocatorLexer lexer, long insertKeyword) throws ParserException, IOException {

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLTableName tableName = parseTableName(lexer);

        return new SQLInsertTriggerEvent(makeContext(), insertKeyword, onKeyword, tableName);
    }

    private SQLUpdateTrigger parseUpdateTrigger(SQLExpressionLexer lexer, long updateKeyword) throws ParserException, IOException {

        final SQLUpdateTriggerEvent event = parseUpdateTriggerEvent(lexer, updateKeyword);

        final BaseSQLTriggerActionClause actionClause;

        if (lexer.peek(SQLToken.REFERENCING)) {

            final long referencingKeyword = lexer.lexKeyword(SQLToken.REFERENCING);

            final SQLOldTriggerCorrelation oldTriggerCorrelation = parseOldTriggerCorrelation(lexer);

            final SQLNewTriggerCorrelation newTriggerCorrelation = lexer.peek(SQLToken.NEW)
                    ? parseNewTriggerCorrelation(lexer)
                    : null;

            actionClause = new SQLTriggerUpdateActionClause(makeContext(), referencingKeyword, oldTriggerCorrelation, newTriggerCorrelation, parseBeforeTriggeredActions(lexer),
                    parseForEachRowTriggeredActions(lexer), parseAfterTriggeredActions(lexer));
        }
        else {
            actionClause = parseActionClause(lexer);
        }

        return new SQLUpdateTrigger(makeContext(), event, actionClause);
    }

    private static SQLUpdateTriggerEvent parseUpdateTriggerEvent(SQLAllocatorLexer lexer, long updateKeyword) throws ParserException, IOException {

        final long ofKeyword;
        final SQLColumnNames columnNames;

        if (lexer.peek(SQLToken.OF)) {

            ofKeyword = lexer.lexKeyword(SQLToken.OF);

            columnNames = parseColumnNames(lexer, lexer.getAllocator(), false);
        }
        else {
            ofKeyword = BaseASTElement.NO_KEYWORD;
            columnNames = null;
        }

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLTableName tableName = parseTableName(lexer);

        return new SQLUpdateTriggerEvent(makeContext(), updateKeyword, ofKeyword, columnNames, onKeyword, tableName);
    }

    private SQLDeleteTrigger parseDeleteTrigger(SQLExpressionLexer lexer, long deleteKeyword) throws ParserException, IOException {

        final SQLDeleteTriggerEvent event = parseDeleteTriggerEvent(lexer, deleteKeyword);

        final BaseSQLTriggerActionClause actionClause = parseSelectOrDeleteAction(lexer);

        return new SQLDeleteTrigger(makeContext(), event, actionClause);
    }

    private static SQLDeleteTriggerEvent parseDeleteTriggerEvent(SQLAllocatorLexer lexer, long deleteKeyword) throws ParserException, IOException {

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLTableName tableName = parseTableName(lexer);

        return new SQLDeleteTriggerEvent(makeContext(), deleteKeyword, onKeyword, tableName);
    }

    private static SQLOldTriggerCorrelation parseOldTriggerCorrelation(SQLLexer lexer) throws ParserException, IOException {

        final long oldKeyword = lexer.lexKeyword(SQLToken.OLD);
        final long asKeyword = lexer.lexKeyword(SQLToken.AS);

        final long correlationName = lexer.lexName();

        return new SQLOldTriggerCorrelation(makeContext(), oldKeyword, asKeyword, correlationName);
    }

    private static SQLNewTriggerCorrelation parseNewTriggerCorrelation(SQLLexer lexer) throws ParserException, IOException {

        final long newKeyword = lexer.lexKeyword(SQLToken.NEW);
        final long asKeyword = lexer.lexKeyword(SQLToken.AS);

        final long correlationName = lexer.lexName();

        return new SQLNewTriggerCorrelation(makeContext(), newKeyword, asKeyword, correlationName);
    }

    private BaseSQLTriggerActionClause parseSelectOrDeleteAction(SQLExpressionLexer lexer) throws ParserException, IOException {

        return lexer.peek(SQLToken.REFERENCING)
                ? parseSelectOrDeleteActionClause(lexer)
                : parseActionClause(lexer);
    }

    private SQLTriggerSelectOrDeleteActionClause parseSelectOrDeleteActionClause(SQLExpressionLexer lexer) throws ParserException, IOException {

        final long referencingKeyword = lexer.lexKeyword(SQLToken.REFERENCING);

        final SQLOldTriggerCorrelation oldTriggerCorrelation = parseOldTriggerCorrelation(lexer);

        return new SQLTriggerSelectOrDeleteActionClause(makeContext(), referencingKeyword, oldTriggerCorrelation, parseBeforeTriggeredActions(lexer),
                parseForEachRowTriggeredActions(lexer), parseAfterTriggeredActions(lexer));
    }

    private SQLTriggerActionClause parseActionClause(SQLExpressionLexer lexer) throws ParserException, IOException {

        return new SQLTriggerActionClause(makeContext(), parseBeforeTriggeredActions(lexer), parseForEachRowTriggeredActions(lexer), parseAfterTriggeredActions(lexer));
    }

    private SQLBeforeTriggeredActions parseBeforeTriggeredActions(SQLExpressionLexer lexer) throws ParserException, IOException {

        return lexer.peek(SQLToken.BEFORE)
                ? new SQLBeforeTriggeredActions(makeContext(), lexer.lexKeyword(SQLToken.BEFORE), parseTriggeredActions(lexer))
                : null;
    }

    private SQLForEachRowTriggeredActions parseForEachRowTriggeredActions(SQLExpressionLexer lexer) throws ParserException, IOException {

        return lexer.peek(SQLToken.FOR)
                ? new SQLForEachRowTriggeredActions(makeContext(), lexer.lexKeyword(SQLToken.FOR), lexer.lexKeyword(SQLToken.EACH), lexer.lexKeyword(SQLToken.ROW),
                        parseTriggeredActions(lexer))
                : null;
    }

    private SQLAfterTriggeredActions parseAfterTriggeredActions(SQLExpressionLexer lexer) throws ParserException, IOException {

        return lexer.peek(SQLToken.AFTER)
                ? new SQLAfterTriggeredActions(makeContext(), lexer.lexKeyword(SQLToken.AFTER), parseTriggeredActions(lexer))
                : null;
    }

    private static final SQLToken[] TRIGGERED_ACTION_TOKENS = new SQLToken[] {

            SQLToken.INSERT,
            SQLToken.UPDATE,
            SQLToken.DELETE
    };

    private IImutableList<SQLTriggeredAction> parseTriggeredActions(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLTriggeredAction> list = allocator.allocateList(100);

        final IImutableList<SQLTriggeredAction> result;

        try {
            for (;;) {

                final long whenKeyword;
                final Expression condition;

                if (lexer.peek(SQLToken.WHEN)) {

                    whenKeyword = lexer.lexKeyword(SQLToken.WHEN);

                    lexer.lexExpect(SQLToken.LPAREN);

                    condition = conditionParser.parseCondition(lexer);

                    lexer.lexExpect(SQLToken.RPAREN);
                }
                else {
                    whenKeyword = BaseASTElement.NO_KEYWORD;
                    condition = null;
                }

                final IAddableList<SQLTriggeredStatement> triggeredStatements = allocator.allocateList(100);

                try {
                    parseTriggeredStatements(lexer, triggeredStatements);

                    final SQLTriggeredAction triggeredAction = new SQLTriggeredAction(makeContext(), whenKeyword, condition, triggeredStatements);

                    list.add(triggeredAction);
                }
                finally {

                    allocator.freeList(triggeredStatements);
                }
                final SQLTriggeredStatement triggeredStatement;

                final SQLToken triggeredStatementToken = lexer.lex(TRIGGERED_ACTION_TOKENS);

                final long triggeredStatementKeyword = lexer.getStringRef();

                switch (triggeredStatementToken) {

                case INSERT:

                    triggeredStatement = insertParser.parseInsert(lexer, triggeredStatementKeyword);
                    break;

                case UPDATE:

                    triggeredStatement = updateParser.parseUpdate(lexer, triggeredStatementKeyword);
                    break;

                case DELETE:

                    triggeredStatement = deleteParser.parseDelete(lexer, triggeredStatementKeyword);
                    break;

                default:
                    throw new UnsupportedOperationException();
                }

                triggeredStatements.add(triggeredStatement);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }

            lexer.lexExpect(SQLToken.RPAREN);

            result = list.toImmutableList();
        }
        finally {

            allocator.freeList(list);
        }

        return result;
    }

    private void parseTriggeredStatements(SQLExpressionLexer lexer, IAddable<SQLTriggeredStatement> dst) throws ParserException, IOException {

        for (;;) {

            final SQLTriggeredStatement triggeredStatement;

            final SQLToken triggeredStatementToken = lexer.lex(TRIGGERED_ACTION_TOKENS);

            final long triggeredStatementKeyword = lexer.getStringRef();

            switch (triggeredStatementToken) {

            case INSERT:

                triggeredStatement = insertParser.parseInsert(lexer, triggeredStatementKeyword);
                break;

            case UPDATE:

                triggeredStatement = updateParser.parseUpdate(lexer, triggeredStatementKeyword);
                break;

            case DELETE:

                triggeredStatement = deleteParser.parseDelete(lexer, triggeredStatementKeyword);
                break;

            default:
                throw new UnsupportedOperationException();
            }

            dst.add(triggeredStatement);

            if (!lexer.lex(SQLToken.COMMA)) {

                break;
            }
        }
    }
}
