package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLDropTriggerStatement extends SQLTriggerStatement {

    public SQLDropTriggerStatement(Context context, long dropKeyword, long triggerKeyword, long triggerName) {
        super(context, dropKeyword, triggerKeyword, triggerName);
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onDropTrigger(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
