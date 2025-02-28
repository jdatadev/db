package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

public final class SQLAsteriskExpression extends BaseSQLExpression {

    public SQLAsteriskExpression(Context context) {
        super(context);
    }

    @Override
    public <T, R> R visit(SQLExpressionVisitor<T, R> visitor, T parameter) {

        return visitor.onAsterisk(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
