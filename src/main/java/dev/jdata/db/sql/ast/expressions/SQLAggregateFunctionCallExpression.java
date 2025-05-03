package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public final class SQLAggregateFunctionCallExpression extends BaseSQLFunctionCallExpression {

    public SQLAggregateFunctionCallExpression(Context context, long functionName, IListGetters<Expression> parameters) {
        super(context, functionName, parameters);
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E  {

        return visitor.onAggregateFunctionCall(this, parameter);
    }
}
