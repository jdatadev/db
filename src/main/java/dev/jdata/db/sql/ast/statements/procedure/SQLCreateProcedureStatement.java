package dev.jdata.db.sql.ast.statements.procedure;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLCreateProcedureStatement extends SQLProcedureStatement {

    public SQLCreateProcedureStatement(Context context, long createKeyword, long procedureKeyword, long name) {
        super(context, createKeyword, procedureKeyword, name);
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onCreateProcedure(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
