package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public class SQLTriggerInsertActionClause extends BaseSQLTriggerReferencingActionClause {

    private final ASTSingle<SQLNewTriggerCorrelation> newTriggerCorrelation;

    public SQLTriggerInsertActionClause(Context context, long referencingKeyword, SQLNewTriggerCorrelation newTriggerCorrelation,
            SQLBeforeTriggeredActions beforeTriggeredActions, SQLForEachRowTriggeredActions forEachRowTriggeredActions, SQLAfterTriggeredActions afterTriggeredActions) {
        super(context, referencingKeyword, beforeTriggeredActions, forEachRowTriggeredActions, afterTriggeredActions);

        this.newTriggerCorrelation = safeMakeSingle(newTriggerCorrelation);
    }

    public final SQLNewTriggerCorrelation getNewTriggerCorrelation() {

        return safeGet(newTriggerCorrelation);
    }
}
