package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

public final class SQLIntegerLiteral extends SQLLiteral {

    private final long integer;

    public SQLIntegerLiteral(Context context, long integer) {
        super(context);

        this.integer = integer;
    }

    public long getInteger() {
        return integer;
    }

    @Override
    public <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter) {

        return visitor.onIntegerLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
