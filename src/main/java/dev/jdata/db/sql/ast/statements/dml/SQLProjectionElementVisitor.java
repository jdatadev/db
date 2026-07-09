package dev.jdata.db.sql.ast.statements.dml;

public interface SQLProjectionElementVisitor<P, R> {

    R onColumn(SQLColumnProjectionElement columnProjectionElement, P parameter);

    R onFunction(SQLFunctionProjectionElement functionProjectionElement, P parameter);
    R onAggregate(SQLAggregateProjectionElement aggregateProjectionElement, P parameter);

    R onExpression(SQLExpressionProjectionElement expressionProjectionElement, P parameter);
}
