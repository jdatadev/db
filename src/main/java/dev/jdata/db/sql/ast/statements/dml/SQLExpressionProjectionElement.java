package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLExpressionProjectionElement extends SQLProjectionElement {

    private final ASTSingle<Expression> expression;

    public SQLExpressionProjectionElement(Context context, Expression expression) {
        super(context);

        this.expression = makeSingle(expression);
    }

    public Expression getExpression() {
        return expression.get();
    }

    @Override
    public <T, R> R visit(SQLProjectionElementVisitor<T, R> visitor, T parameter) {

        return visitor.onExpression(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(expression, recurseMode, iterator);
    }
}
