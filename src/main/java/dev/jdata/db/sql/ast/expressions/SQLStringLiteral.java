package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

public final class SQLStringLiteral extends SQLLiteral {

    private final long string;

    public SQLStringLiteral(Context context, long string) {
        super(context);

        this.string = checkIsString(string);
    }

    public long getString() {
        return string;
    }

    @Override
    public <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter) {

        return visitor.onStringLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
