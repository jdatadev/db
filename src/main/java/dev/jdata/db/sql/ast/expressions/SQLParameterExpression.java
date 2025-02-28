package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

public final class SQLParameterExpression extends BaseSQLExpression {

    private final long parameter;

    public SQLParameterExpression(Context context, long parameter) {
        super(context);

        this.parameter = checkIsString(parameter);
    }

    public long getParameter() {
        return parameter;
    }

    @Override
    public <T, R> R visit(SQLExpressionVisitor<T, R> visitor, T parameter) {

        return visitor.onParameter(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
