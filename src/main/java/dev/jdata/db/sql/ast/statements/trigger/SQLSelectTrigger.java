package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public class SQLSelectTrigger extends BaseSQLTrigger<SQLSelectTriggerEvent> {

    public SQLSelectTrigger(Context context, SQLSelectTriggerEvent event, BaseSQLTriggerActionClause actionClause) {
        super(context, event, actionClause);
    }
}
