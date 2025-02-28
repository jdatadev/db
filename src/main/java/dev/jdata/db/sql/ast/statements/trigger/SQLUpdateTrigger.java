package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLUpdateTrigger extends BaseSQLTrigger<SQLUpdateTriggerEvent> {

    public SQLUpdateTrigger(Context context, SQLUpdateTriggerEvent event, BaseSQLTriggerActionClause actionClause) {
        super(context, event, actionClause);
    }
}
