package dev.jdata.db.sql.ast.expressions;

import java.util.List;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

public final class SQLFunctionCallExpression extends BaseSQLFunctionCallExpression {

    public SQLFunctionCallExpression(Context context, long functionName, List<Expression> parameters) {
        super(context, functionName, parameters);
    }

    @Override
    public <T, R> R visit(SQLExpressionVisitor<T, R> visitor, T parameter) {

        return visitor.onFunctionCall(this, parameter);
    }
}
