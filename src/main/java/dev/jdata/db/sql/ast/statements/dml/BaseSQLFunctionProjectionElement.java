package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.BaseSQLFunctionCallExpression;

abstract class BaseSQLFunctionProjectionElement<T extends BaseSQLFunctionCallExpression> extends SQLProjectionElement {

    private final ASTSingle<T> functionCallExpression;

    BaseSQLFunctionProjectionElement(Context context, T functionCallExpression) {
        super(context);

        this.functionCallExpression = makeSingle(functionCallExpression);
    }

    public final T getFunctionCallExpression() {
        return functionCallExpression.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(functionCallExpression, recurseMode, iterator);
    }
}
