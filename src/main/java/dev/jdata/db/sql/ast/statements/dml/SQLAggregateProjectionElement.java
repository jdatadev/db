package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLAggregateFunctionCallExpression;

public final class SQLAggregateProjectionElement extends BaseSQLFunctionProjectionElement<SQLAggregateFunctionCallExpression> {

    public SQLAggregateProjectionElement(Context context, SQLAggregateFunctionCallExpression functionCallExpression) {
        super(context, functionCallExpression);
    }

    @Override
    public <P, R> R visit(SQLProjectionElementVisitor<P, R> visitor, P parameter) {

        return visitor.onAggregate(this, parameter);
    }
}
