package dev.jdata.db.sql.ast.conditions;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

abstract class BaseSQLInCondition extends BaseSQLCondition {

    private final long inKeyword;
    private final ASTList<Expression> expressions;

    BaseSQLInCondition(Context context, long inKeyword, List<Expression> expressions) {
        super(context);

        this.inKeyword = checkIsKeyword(inKeyword);
        this.expressions = makeList(expressions);
    }

    public final long getInKeyword() {
        return inKeyword;
    }

    public final ASTList<Expression> getExpressions() {
        return expressions;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(expressions, recurseMode, iterator);
    }
}
