package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLFunctionCallExpression;

public final class SQLFunctionProjectionElement extends BaseSQLFunctionProjectionElement<SQLFunctionCallExpression> {

    public SQLFunctionProjectionElement(Context context, SQLFunctionCallExpression functionCallExpression) {
        super(context, functionCallExpression);
    }

    @Override
    public <P, R> R visit(SQLProjectionElementVisitor<P, R> visitor, P parameter) {

        return visitor.onFunction(this, parameter);
    }
}
