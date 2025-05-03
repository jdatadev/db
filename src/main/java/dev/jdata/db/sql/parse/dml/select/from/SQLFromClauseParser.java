package dev.jdata.db.sql.parse.dml.select.from;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLFromClause;
import dev.jdata.db.sql.ast.statements.dml.SQLFromTable;
import dev.jdata.db.sql.ast.statements.dml.SQLJoin;
import dev.jdata.db.sql.ast.statements.dml.SQLJoinFromTable;
import dev.jdata.db.sql.ast.statements.dml.SQLJoinType;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectNameAndAlias;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectNameFromTable;
import dev.jdata.db.sql.ast.statements.dml.SQLTableJoin;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;

public class SQLFromClauseParser extends BaseSQLParser {

    private final SQLConditionParser conditionParser;

    public SQLFromClauseParser(SQLConditionParser conditionParser) {

        this.conditionParser = Objects.requireNonNull(conditionParser);
    }

    public final SQLFromClause parseFromClause(SQLExpressionLexer lexer) throws ParserException, IOException {

        Objects.requireNonNull(lexer);

        final SQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLFromTable> fromTables = allocator.allocateList(10);

        try {
            for (;;) {

                final SQLFromTable fromTable = parseFromTable(lexer);

                fromTables.add(fromTable);

                if (!lexer.peek(SQLToken.COMMA)) {

                    break;
                }
            }
        }
        finally {

            allocator.freeList(fromTables);
        }

        return new SQLFromClause(makeContext(), fromTables);
    }

    private SQLFromTable parseFromTable(SQLExpressionLexer lexer) throws ParserException, IOException {

        final long tableName = lexer.lexName();

        return parseFromTable(lexer, new SQLObjectName(makeContext(), tableName));
    }

    private SQLFromTable parseFromTable(SQLExpressionLexer lexer, SQLObjectName tableObjectName) throws ParserException, IOException {

        final SQLFromTable fromTable;

        if (lexer.peek(SQLToken.COMMA)) {

            fromTable = new SQLObjectNameFromTable(makeContext(), new SQLObjectNameAndAlias(makeContext(), tableObjectName));
        }
        else {
            final SQLObjectNameAndAlias objectNameAndAlias = parseObjectNameAndAlias(lexer, tableObjectName);

            if (lexer.peek(SQLToken.COMMA)) {

                fromTable = new SQLObjectNameFromTable(makeContext(), objectNameAndAlias);
            }
            else {
                fromTable = parseJoinFromTable(lexer, objectNameAndAlias);
            }
        }

        return fromTable;
    }

    private SQLObjectNameAndAlias parseObjectNameAndAlias(SQLExpressionLexer lexer, SQLObjectName tableObjectName) throws ParserException, IOException {

        final SQLObjectNameAndAlias result;

        if (lexer.peek(SQLToken.NAME)) {

            final long alias = lexer.lexName();

            result = new SQLObjectNameAndAlias(makeContext(), tableObjectName, alias);
        }
        else {
            result = new SQLObjectNameAndAlias(tableObjectName.getContext(), tableObjectName);
        }

        return result;
    }

    private static final SQLToken[] AFTER_FROM_TABLE_TOKENS = new SQLToken[] {

            SQLToken.INNER,
            SQLToken.LEFT,
            SQLToken.RIGHT,
            SQLToken.FULL,
            SQLToken.JOIN
    };

    private SQLJoinFromTable parseJoinFromTable(SQLExpressionLexer lexer, SQLObjectNameAndAlias tableNameAndAlias) throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

        final SQLJoinFromTable result;

        final IAddableList<SQLTableJoin> tableJoins = allocator.allocateList(10);

        try {
            for (;;) {

                final SQLTableJoin tableJoin = parseTableJoin(lexer);

                if (tableJoin == null) {

                    break;
                }

                tableJoins.add(tableJoin);
            }

            final long onKeyword = lexer.lexKeyword(SQLToken.ON);

            final Expression onCondition = conditionParser.parseCondition(lexer);

            result = new SQLJoinFromTable(makeContext(), tableNameAndAlias, onKeyword, onCondition, tableJoins);
        }
        finally {

            allocator.freeList(tableJoins);
        }

        return result;
    }

    private SQLTableJoin parseTableJoin(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLJoin join = parseJoin(lexer);

        final long tableName = lexer.lexName();
        final SQLObjectName tableObjectName = new SQLObjectName(makeContext(), tableName);

        final SQLObjectNameAndAlias objectNameAndAlias = parseObjectNameAndAlias(lexer, tableObjectName);

        final SQLJoinFromTable joinFromTable = parseJoinFromTable(lexer, objectNameAndAlias);

        return new SQLTableJoin(makeContext(), join, joinFromTable);
    }

    private static SQLJoin parseJoin(SQLLexer lexer) throws ParserException, IOException {

        final SQLToken[] expectedTokens = AFTER_FROM_TABLE_TOKENS;

        final SQLJoin join;

        switch (lexer.lex(expectedTokens)) {

        case INNER: {

            final long innerKeyword = lexer.getStringRef();
            final long joinKeyword = lexer.lexKeyword(SQLToken.JOIN);

            join = new SQLJoin(makeContext(), innerKeyword, joinKeyword, SQLJoinType.INNER);
            break;
        }

        case JOIN: {

            final long joinKeyword = lexer.lexKeyword(SQLToken.JOIN);

            join = new SQLJoin(makeContext(), joinKeyword, SQLJoinType.INNER);
            break;
        }

        case LEFT: {

            final long leftKeyword = lexer.getStringRef();

            final SQLJoinType joinType = SQLJoinType.LEFT_OUTER;

            final Context context = makeContext();

            join = lexer.peek(SQLToken.OUTER)
                    ? new SQLJoin(context, leftKeyword, lexer.lexKeyword(SQLToken.OUTER), joinType)
                    : new SQLJoin(context, leftKeyword, joinType);
            break;
        }

        case RIGHT: {

            final long rightKeyword = lexer.getStringRef();

            final SQLJoinType joinType = SQLJoinType.RIGHT_OUTER;

            final Context context = makeContext();

            join = lexer.peek(SQLToken.OUTER)
                    ? new SQLJoin(context, rightKeyword, lexer.lexKeyword(SQLToken.OUTER), joinType)
                    : new SQLJoin(context, rightKeyword, joinType);
            break;
        }

        case FULL: {

            final long fullKeyword = lexer.getStringRef();

            final SQLJoinType joinType = SQLJoinType.FULL_OUTER;

            final Context context = makeContext();

            join = lexer.peek(SQLToken.OUTER)
                    ? new SQLJoin(context, fullKeyword, lexer.lexKeyword(SQLToken.OUTER), joinType)
                    : new SQLJoin(context, fullKeyword, joinType);
            break;
        }

        default:
            throw new UnsupportedOperationException();
        }

        return join;
    }
}
