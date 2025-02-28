package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLDeleteTrigger extends BaseSQLTrigger<SQLDeleteTriggerEvent> {

    public SQLDeleteTrigger(Context context, SQLDeleteTriggerEvent event, BaseSQLTriggerActionClause actionClause) {
        super(context, event, actionClause);
    }
}
