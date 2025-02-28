package dev.jdata.db.sql.parse.dml.select.result.groupby;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLGroupByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLOrderByOrGroupByItem;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItemOrderByOrGroupByItem;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionItemParser;
import dev.jdata.db.sql.parse.dml.select.result.BaseSQLGroupOrOrderByClauseParser;

public class SQLGroupByClauseParser extends BaseSQLGroupOrOrderByClauseParser<SQLOrderByOrGroupByItem, SQLGroupByClause> {

    public SQLGroupByClauseParser(SQLProjectionItemParser projectionItemParser) {
        super(projectionItemParser);
    }

    public final SQLGroupByClause parseGroupBy(SQLExpressionLexer lexer) throws ParserException, IOException {

        Objects.requireNonNull(lexer);

        return parseGroupByOrOrderBy(lexer);
    }

    @Override
    protected SQLOrderByOrGroupByItem parseElement(SQLExpressionLexer lexer, Context context, SQLProjectionItem projectionItem) throws IOException {

        return new SQLProjectionItemOrderByOrGroupByItem(context, projectionItem);
    }

    @Override
    protected SQLGroupByClause createGroupOrOrderByClause(Context context, List<SQLOrderByOrGroupByItem> elements) {

        return new SQLGroupByClause(context, elements);
    }
}
