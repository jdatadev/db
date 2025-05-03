package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public abstract class BaseSQLFunctionCallExpression extends BaseSQLExpression {

    private final long functionName;
    private final ASTList<Expression> parameters;

    BaseSQLFunctionCallExpression(Context context, long functionName, IListGetters<Expression> parameters) {
        super(context);

        this.functionName = functionName;
        this.parameters = parameters.isEmpty() ? emptyList() : makeList(parameters);
    }

    public final long getFunctionName() {
        return functionName;
    }

    public final ASTList<Expression> getParameters() {
        return parameters;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(parameters, recurseMode, iterator);
    }
}
