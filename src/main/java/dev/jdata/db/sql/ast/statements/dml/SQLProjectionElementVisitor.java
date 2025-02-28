package dev.jdata.db.sql.ast.statements.dml;

public interface SQLProjectionElementVisitor<T, R> {

    R onColumn(SQLColumnProjectionElement columnProjectionElement, T parameter);

    R onFunction(SQLFunctionProjectionElement functionProjectionElement, T parameter);
    R onAggregate(SQLAggregateProjectionElement aggregateProjectionElement, T parameter);

    R onExpression(SQLExpressionProjectionElement expressionProjectionElement, T parameter);
}
