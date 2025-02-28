package dev.jdata.db.sql.ast.expressions;

public interface SQLExpressionVisitor<P, R> {

    R onIntegerLiteral(SQLIntegerLiteral integerLiteral, P parameter);
    R onLargeIntegerLiteral(SQLLargeIntegerLiteral largeIntegerLiteral, P parameter);
    R onDecimalLiteral(SQLDecimalLiteral decimalLiteral, P parameter);
    R onStringLiteral(SQLStringLiteral stringLiteral, P parameter);

    R onColumn(SQLColumnExpression columnExpression, P parameter);

    R onParameter(SQLParameterExpression parameterExpression, P parameter);

    R onFunctionCall(SQLFunctionCallExpression functionCallExpression, P parameter);
    R onAggregateFunctionCall(SQLAggregateFunctionCallExpression aggregateFunctionCallExpression, P parameter);

    R onAsterisk(SQLAsteriskExpression asteriskExpression, P parameter);

    R onSubSelect(SQLSubSelectExpression subSelectExpression, P parameter);
}
