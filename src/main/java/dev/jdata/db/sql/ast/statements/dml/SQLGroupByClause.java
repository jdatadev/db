package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLGroupByClause extends SQLOrderByOrGroupByClause<SQLOrderByOrGroupByItem> {

    public SQLGroupByClause(Context context, IIndexListGetters<SQLOrderByOrGroupByItem> items) {
        super(context, items);
    }
}
