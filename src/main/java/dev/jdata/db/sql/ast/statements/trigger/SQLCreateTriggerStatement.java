package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLCreateTriggerStatement extends SQLTriggerStatement {

    private final ASTSingle<BaseSQLTrigger<?>> trigger;

    public SQLCreateTriggerStatement(Context context, long createKeyword, long triggerKeyword, long triggerName, BaseSQLTrigger<?> trigger) {
        super(context, createKeyword, triggerKeyword, triggerName);

        this.trigger = makeSingle(trigger);
    }

    public BaseSQLTrigger<?> getTrigger() {
        return trigger.get();
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onCreateTrigger(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(trigger, recurseMode, iterator);
    }
}
