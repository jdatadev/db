package dev.jdata.db.sql.ast.expressions;

public interface SQLExpressionVisitor<P, R, E extends Exception> {

    R onIntegerLiteral(SQLIntegerLiteral integerLiteral, P parameter) throws E;
    R onLargeIntegerLiteral(SQLLargeIntegerLiteral largeIntegerLiteral, P parameter) throws E;
    R onDecimalLiteral(SQLDecimalLiteral decimalLiteral, P parameter) throws E;
    R onStringLiteral(SQLStringLiteral stringLiteral, P parameter) throws E;

    R onColumn(SQLColumnExpression columnExpression, P parameter) throws E;

    R onParameter(SQLParameterExpression parameterExpression, P parameter) throws E;

    R onFunctionCall(SQLFunctionCallExpression functionCallExpression, P parameter) throws E;
    R onAggregateFunctionCall(SQLAggregateFunctionCallExpression aggregateFunctionCallExpression, P parameter) throws E;

    R onAsterisk(SQLAsteriskExpression asteriskExpression, P parameter) throws E;

    R onSubSelect(SQLSubSelectExpression subSelectExpression, P parameter) throws E;
}
