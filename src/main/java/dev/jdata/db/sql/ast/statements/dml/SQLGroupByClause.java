package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.parse.context.Context;

public final class SQLGroupByClause extends SQLOrderByOrGroupByClause<SQLOrderByOrGroupByItem> {

    public SQLGroupByClause(Context context, List<SQLOrderByOrGroupByItem> items) {
        super(context, items);
    }
}
