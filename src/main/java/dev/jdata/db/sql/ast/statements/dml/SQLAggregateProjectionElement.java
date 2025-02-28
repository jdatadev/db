package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLAggregateFunctionCallExpression;

public final class SQLAggregateProjectionElement extends BaseSQLFunctionProjectionElement<SQLAggregateFunctionCallExpression> {

    public SQLAggregateProjectionElement(Context context, SQLAggregateFunctionCallExpression functionCallExpression) {
        super(context, functionCallExpression);
    }

    @Override
    public <T, R> R visit(SQLProjectionElementVisitor<T, R> visitor, T parameter) {

        return visitor.onAggregate(this, parameter);
    }
}
