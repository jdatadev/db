package dev.jdata.db.sql.ast.statements.function;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLCreateFunctionStatement extends SQLFunctionStatement {

    public SQLCreateFunctionStatement(Context context, long createKeyword, long functionKeyword, long name) {
        super(context, createKeyword, functionKeyword, name);
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onCreateFunction(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
