package dev.jdata.db.sql.ast.statements.function;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLDropFunctionStatement extends SQLFunctionStatement {

    public SQLDropFunctionStatement(Context context, long dropKeyword, long functionKeyword, long name) {
        super(context, dropKeyword, functionKeyword, name);
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onDropFunction(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
