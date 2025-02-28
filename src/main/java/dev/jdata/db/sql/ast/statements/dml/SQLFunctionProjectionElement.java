package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLFunctionCallExpression;

public final class SQLFunctionProjectionElement extends BaseSQLFunctionProjectionElement<SQLFunctionCallExpression> {

    public SQLFunctionProjectionElement(Context context, SQLFunctionCallExpression functionCallExpression) {
        super(context, functionCallExpression);
    }

    @Override
    public <T, R> R visit(SQLProjectionElementVisitor<T, R> visitor, T parameter) {

        return visitor.onFunction(this, parameter);
    }
}
