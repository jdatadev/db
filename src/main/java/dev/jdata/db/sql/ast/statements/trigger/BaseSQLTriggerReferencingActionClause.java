package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

abstract class BaseSQLTriggerReferencingActionClause extends BaseSQLTriggerActionClause {

    private final long referencingKeyword;

    public BaseSQLTriggerReferencingActionClause(Context context, long referencingKeyword, SQLBeforeTriggeredActions beforeTriggeredActions,
            SQLForEachRowTriggeredActions forEachRowTriggeredActions, SQLAfterTriggeredActions afterTriggeredActions) {
        super(context, beforeTriggeredActions, forEachRowTriggeredActions, afterTriggeredActions);

        this.referencingKeyword = checkIsKeyword(referencingKeyword);
    }

    public final long getReferencingKeyword() {
        return referencingKeyword;
    }
}
