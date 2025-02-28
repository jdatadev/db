package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public class SQLTriggerUpdateActionClause extends BaseSQLTriggerReferencingActionClause {

    private final ASTSingle<SQLOldTriggerCorrelation> oldTriggerCorrelation;
    private final ASTSingle<SQLNewTriggerCorrelation> newTriggerCorrelation;

    public SQLTriggerUpdateActionClause(Context context, long referencingKeyword, SQLOldTriggerCorrelation oldTriggerCorrelation, SQLNewTriggerCorrelation newTriggerCorrelation,
            SQLBeforeTriggeredActions beforeTriggeredActions, SQLForEachRowTriggeredActions forEachRowTriggeredActions, SQLAfterTriggeredActions afterTriggeredActions) {
        super(context, referencingKeyword, beforeTriggeredActions, forEachRowTriggeredActions, afterTriggeredActions);

        this.oldTriggerCorrelation = makeSingle(oldTriggerCorrelation);
        this.newTriggerCorrelation = safeMakeSingle(newTriggerCorrelation);
    }

    public final SQLOldTriggerCorrelation getOldTriggerCorrelation() {
        return oldTriggerCorrelation.get();
    }

    public final SQLNewTriggerCorrelation getNewTriggerCorrelation() {

        return safeGet(newTriggerCorrelation);
    }
}
