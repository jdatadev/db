package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLOrderByClause extends SQLOrderByOrGroupByClause<SQLOrderByItem> {

    public SQLOrderByClause(Context context, IIndexListGetters<SQLOrderByItem> items) {
        super(context, items);
    }
}
