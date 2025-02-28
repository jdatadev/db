package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public class SQLTriggerSelectOrDeleteActionClause extends BaseSQLTriggerReferencingActionClause {

    private final ASTSingle<SQLOldTriggerCorrelation> oldTriggerCorrelation;

    public SQLTriggerSelectOrDeleteActionClause(Context context, long referencingKeyword, SQLOldTriggerCorrelation oldTriggerCorrelation,
            SQLBeforeTriggeredActions beforeTriggeredActions, SQLForEachRowTriggeredActions forEachRowTriggeredActions, SQLAfterTriggeredActions afterTriggeredActions) {
        super(context, referencingKeyword, beforeTriggeredActions, forEachRowTriggeredActions, afterTriggeredActions);

        this.oldTriggerCorrelation = makeSingle(oldTriggerCorrelation);
    }

    public final SQLOldTriggerCorrelation getOldTriggerCorrelation() {
        return oldTriggerCorrelation.get();
    }
}
