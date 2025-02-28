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
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onDropFunction(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
