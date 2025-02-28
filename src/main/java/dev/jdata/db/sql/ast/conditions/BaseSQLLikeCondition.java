package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

abstract class BaseSQLLikeCondition extends BaseSQLCondition {

    private final long likeKeyword;
    private final ASTSingle<Expression> expression;

    BaseSQLLikeCondition(Context context, long likeKeyword, Expression expression) {
        super(context);

        this.likeKeyword = checkIsKeyword(likeKeyword);
        this.expression = makeSingle(expression);
    }

    public final long getLikeKeyword() {
        return likeKeyword;
    }

    public final Expression getExpression() {
        return expression.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(expression, recurseMode, iterator);
    }
}
