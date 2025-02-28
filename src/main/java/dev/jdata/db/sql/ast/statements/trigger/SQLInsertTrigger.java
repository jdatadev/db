package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLInsertTrigger extends BaseSQLTrigger<SQLInsertTriggerEvent> {

    public SQLInsertTrigger(Context context, SQLInsertTriggerEvent event, BaseSQLTriggerActionClause actionClause) {
        super(context, event, actionClause);
    }
}
