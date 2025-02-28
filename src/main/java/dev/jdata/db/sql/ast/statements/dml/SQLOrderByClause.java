package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.parse.context.Context;

public final class SQLOrderByClause extends SQLOrderByOrGroupByClause<SQLOrderByItem> {

    public SQLOrderByClause(Context context, List<SQLOrderByItem> items) {
        super(context, items);
    }
}
