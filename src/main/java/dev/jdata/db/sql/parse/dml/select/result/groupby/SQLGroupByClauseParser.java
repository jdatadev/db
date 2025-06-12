package dev.jdata.db.sql.parse.dml.select.result.groupby;

import java.util.Objects;

import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.io.strings.CharInput;
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

    public final <E extends Exception, I extends CharInput<E>> SQLGroupByClause parseGroupBy(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        Objects.requireNonNull(lexer);

        return parseGroupByOrOrderBy(lexer);
    }

    @Override
    protected <E extends Exception, I extends CharInput<E>> SQLOrderByOrGroupByItem parseElement(SQLExpressionLexer<E, I> lexer, Context context,
            SQLProjectionItem projectionItem) throws E {

        return new SQLProjectionItemOrderByOrGroupByItem(context, projectionItem);
    }

    @Override
    protected SQLGroupByClause createGroupOrOrderByClause(Context context, IIndexListGetters<SQLOrderByOrGroupByItem> elements) {

        return new SQLGroupByClause(context, elements);
    }
}
