package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLTriggerActionClause extends BaseSQLTriggerActionClause {

    public SQLTriggerActionClause(Context context, SQLBeforeTriggeredActions beforeTriggeredActions, SQLForEachRowTriggeredActions forEachRowTriggeredActions,
            SQLAfterTriggeredActions afterTriggeredActions) {
        super(context, beforeTriggeredActions, forEachRowTriggeredActions, afterTriggeredActions);
    }
}
