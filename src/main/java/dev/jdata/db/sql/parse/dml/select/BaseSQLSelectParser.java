package dev.jdata.db.sql.parse.dml.select;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.BaseSQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLFromClause;
import dev.jdata.db.sql.ast.statements.dml.SQLGroupByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLHavingClause;
import dev.jdata.db.sql.ast.statements.dml.SQLOrderByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionClause;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatementPart;
import dev.jdata.db.sql.ast.statements.dml.SQLUnion;
import dev.jdata.db.sql.ast.statements.dml.SQLUnionType;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.dml.select.from.SQLFromClauseParser;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionClauseParser;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionItemParser;
import dev.jdata.db.sql.parse.dml.select.result.groupby.SQLGroupByClauseParser;
import dev.jdata.db.sql.parse.dml.select.result.having.SQLHavingClauseParser;
import dev.jdata.db.sql.parse.dml.select.result.orderby.SQLOrderByClauseParser;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public abstract class BaseSQLSelectParser<T extends BaseSQLSelectStatement> extends SQLStatementParser {

    private final SQLProjectionClauseParser projectionClauseParser;
    private final SQLFromClauseParser fromClauseParser;
    private final SQLWhereClauseParser whereClauseParser;
    private final SQLGroupByClauseParser groupByClauseParser;
    private final SQLHavingClauseParser havingClauseParser;
    private final SQLOrderByClauseParser orderByClauseParser;

    protected abstract T createSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions);

    protected BaseSQLSelectParser(SQLExpressionParser expressionParser, SQLConditionParser conditionParser, SQLWhereClauseParser whereClauseParser) {

        Objects.requireNonNull(expressionParser);
        Objects.requireNonNull(conditionParser);
        Objects.requireNonNull(whereClauseParser);

        final SQLProjectionItemParser projectionItemParser = new SQLProjectionItemParser(expressionParser);

        this.projectionClauseParser = new SQLProjectionClauseParser(projectionItemParser);
        this.fromClauseParser = new SQLFromClauseParser(conditionParser);
        this.whereClauseParser = whereClauseParser;
        this.groupByClauseParser = new SQLGroupByClauseParser(projectionItemParser);
        this.havingClauseParser = new SQLHavingClauseParser(conditionParser);
        this.orderByClauseParser = new SQLOrderByClauseParser(projectionItemParser);
    }

    private static final SQLToken[] UNION_TOKENS = new SQLToken[] {

            SQLToken.UNION
    };

    public final T parseSelect(SQLExpressionLexer lexer, long selectKeyword) throws ParserException, IOException {

        final int selectPartsInitialCapacity = 10;

        final SQLAllocator allocator = lexer.getAllocator();

        final T result;

        final List<SQLSelectStatementPart> selectStatementParts = allocator.allocateList(selectPartsInitialCapacity);
        final List<SQLUnion> unions = allocator.allocateList(selectPartsInitialCapacity - 1);

        final SQLToken[] expectedUnionTokens = UNION_TOKENS;

        long keyword = selectKeyword;

        try {
            for (;;) {

                final SQLSelectStatementPart selectStatementPart = parseSelectStatementPart(lexer, keyword);

                selectStatementParts.add(selectStatementPart);

                if (lexer.peek(expectedUnionTokens) != SQLToken.NONE) {

                    final SQLToken token = lexer.lex(expectedUnionTokens);

                    final long unionKeyword = lexer.getStringRef();

                    final SQLUnionType unionType;

                    switch (token) {

                    case UNION:

                        unionType = SQLUnionType.UNION;
                        break;

                    default:
                        throw new UnsupportedOperationException();
                    }

                    final SQLUnion union = new SQLUnion(makeContext(), unionKeyword, unionType);

                    unions.add(union);

                    if (lexer.peek(SQLToken.SELECT)) {

                        keyword = lexer.lexKeyword(SQLToken.SELECT);
                    }
                }
                else {
                    break;
                }
            }

            result = createSelectStatement(makeContext(), selectStatementParts, unions);
        }
        finally {

            allocator.freeList(selectStatementParts);
            allocator.freeList(unions);
        }

        return result;
    }

    private SQLSelectStatementPart parseSelectStatementPart(SQLExpressionLexer lexer, long selectKeyword) throws ParserException, IOException {

        final SQLProjectionClause projectionClause = projectionClauseParser.parseProjectionClause(lexer);

        final SQLFromClause fromClause = fromClauseParser.parseFromClause(lexer);

        final SQLWhereClause whereClause = lexer.peek(SQLToken.WHERE)
                ? whereClauseParser.parseWhereClause(lexer)
                : null;

        final SQLGroupByClause groupByClause;
        final SQLHavingClause havingClause;

        if (lexer.peek(SQLToken.GROUP)) {

            groupByClause = groupByClauseParser.parseGroupBy(lexer);

            if (lexer.peek(SQLToken.HAVING)) {

                havingClause = havingClauseParser.parseHavingClause(lexer);
            }
            else {
                havingClause = null;
            }
        }
        else {
            groupByClause = null;
            havingClause = null;
        }

        final SQLOrderByClause orderByClause = lexer.peek(SQLToken.ORDER)
                ? orderByClauseParser.parseOrderBy(lexer)
                : null;

        return new SQLSelectStatementPart(makeContext(), selectKeyword, projectionClause, fromClause, whereClause, groupByClause, havingClause, orderByClause);
    }
}
