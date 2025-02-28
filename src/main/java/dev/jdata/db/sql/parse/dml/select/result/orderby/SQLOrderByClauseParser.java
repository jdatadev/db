package dev.jdata.db.sql.parse.dml.select.result.orderby;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLOrderByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLOrderByItem;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItemOrderByOrGroupByItem;
import dev.jdata.db.sql.ast.statements.dml.SQLSortOrder;
import dev.jdata.db.sql.ast.statements.dml.SQLSortOrderType;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionItemParser;
import dev.jdata.db.sql.parse.dml.select.result.BaseSQLGroupOrOrderByClauseParser;

public class SQLOrderByClauseParser extends BaseSQLGroupOrOrderByClauseParser<SQLOrderByItem, SQLOrderByClause> {

    public SQLOrderByClauseParser(SQLProjectionItemParser projectionItemParser) {
        super(projectionItemParser);
    }

    public final SQLOrderByClause parseOrderBy(SQLExpressionLexer lexer) throws ParserException, IOException {

        Objects.requireNonNull(lexer);

        return parseGroupByOrOrderBy(lexer);
    }

    private static final SQLToken[] ORDER_TOKENS = new SQLToken[] {

            SQLToken.ASCENDING,
            SQLToken.DESCENDING
    };

    @Override
    protected SQLOrderByItem parseElement(SQLExpressionLexer lexer, Context context, SQLProjectionItem projectionItem) throws IOException {

        final SQLProjectionItemOrderByOrGroupByItem projectionItemOrderByOrGroupByItem = new SQLProjectionItemOrderByOrGroupByItem(context, projectionItem);

        final SQLToken[] expectedTokens = ORDER_TOKENS;

        final SQLOrderByItem result;

        if (lexer.peek(expectedTokens) != SQLToken.NONE) {

            final SQLToken sortOrderToken = lexer.lex(expectedTokens);
            final long sortOrderKeyword = lexer.getStringRef();

            final SQLSortOrderType sortOrderType;

            switch (sortOrderToken) {

            case ASCENDING:

                sortOrderType = SQLSortOrderType.ASCENDING;
                break;

            case DESCENDING:

                sortOrderType = SQLSortOrderType.DESCENDING;
                break;

            default:
                throw new UnsupportedOperationException();
            }

            final SQLSortOrder sortOrder = new SQLSortOrder(makeContext(), sortOrderKeyword, sortOrderType);

            result = new SQLOrderByItem(makeContext(), projectionItemOrderByOrGroupByItem, sortOrder);
        }
        else {
            result = new SQLOrderByItem(makeContext(), projectionItemOrderByOrGroupByItem);
        }

        return result;
    }

    @Override
    protected SQLOrderByClause createGroupOrOrderByClause(Context context, List<SQLOrderByItem> elements) {

        return new SQLOrderByClause(context, elements);
    }
}
