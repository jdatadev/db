package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class BaseSQLTrigger<T extends SQLTriggerEvent> extends BaseSQLElement {

    private final ASTSingle<T> event;
    private final ASTSingle<BaseSQLTriggerActionClause> actionClause;

    BaseSQLTrigger(Context context, T event, BaseSQLTriggerActionClause actionClause) {
        super(context);

        this.event = makeSingle(event);
        this.actionClause = makeSingle(actionClause);
    }

    public final T getEvent() {
        return event.get();
    }

    public final BaseSQLTriggerActionClause getActionClause() {
        return actionClause.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(event, recurseMode, iterator);
        doIterate(actionClause, recurseMode, iterator);
    }
}
