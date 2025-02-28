package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class BaseSQLTriggerActionClause extends BaseSQLElement {

    private final ASTSingle<SQLBeforeTriggeredActions> beforeTriggeredActions;
    private final ASTSingle<SQLForEachRowTriggeredActions> forEachRowTriggeredActions;
    private final ASTSingle<SQLAfterTriggeredActions> afterTriggeredActions;

    BaseSQLTriggerActionClause(Context context, SQLBeforeTriggeredActions beforeTriggeredActions, SQLForEachRowTriggeredActions forEachRowTriggeredActions,
            SQLAfterTriggeredActions afterTriggeredActions) {
        super(context);

        this.beforeTriggeredActions = safeMakeSingle(beforeTriggeredActions);
        this.forEachRowTriggeredActions = safeMakeSingle(forEachRowTriggeredActions);
        this.afterTriggeredActions = safeMakeSingle(afterTriggeredActions);
    }

    public final SQLBeforeTriggeredActions getBeforeTriggeredActions() {
        return beforeTriggeredActions.get();
    }

    public final SQLForEachRowTriggeredActions getForEachRowTriggeredActions() {
        return forEachRowTriggeredActions.get();
    }

    public final SQLAfterTriggeredActions getAfterTriggeredActions() {
        return afterTriggeredActions.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        if (beforeTriggeredActions != null) {

            doIterate(beforeTriggeredActions, recurseMode, iterator);
        }

        if (forEachRowTriggeredActions != null) {

            doIterate(forEachRowTriggeredActions, recurseMode, iterator);
        }

        if (afterTriggeredActions != null) {

            doIterate(afterTriggeredActions, recurseMode, iterator);
        }
    }
}
