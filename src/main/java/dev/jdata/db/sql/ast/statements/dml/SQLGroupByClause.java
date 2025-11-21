package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IIndexListView;
import org.jutils.parse.context.Context;

public final class SQLGroupByClause extends SQLOrderByOrGroupByClause<SQLOrderByOrGroupByItem> {

    public SQLGroupByClause(Context context, IIndexListView<SQLOrderByOrGroupByItem> items) {
        super(context, items);
    }
}
