package dev.jdata.db.sql.ast.statements.index;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLDropIndexStatement extends SQLIndexStatement {

    public SQLDropIndexStatement(Context context, long operationKeyword, long indexKeyword, long name) {
        super(context, operationKeyword, indexKeyword, name);
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onDropIndex(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
