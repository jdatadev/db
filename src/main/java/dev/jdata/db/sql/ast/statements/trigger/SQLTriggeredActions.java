package dev.jdata.db.sql.ast.statements.trigger;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

public abstract class SQLTriggeredActions extends BaseASTElement {

    private final ASTList<SQLTriggeredAction> actions;

    SQLTriggeredActions(Context context, List<SQLTriggeredAction> actions) {
        super(context);

        this.actions = makeList(actions);
    }

    public final ASTList<SQLTriggeredAction> getActions() {
        return actions;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(actions, recurseMode, iterator);
    }
}
