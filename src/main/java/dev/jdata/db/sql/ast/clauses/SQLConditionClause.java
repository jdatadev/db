package dev.jdata.db.sql.ast.clauses;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public abstract class SQLConditionClause extends SQLClause {

    private final long keyword;
    private final ASTSingle<Expression> condition;

    protected SQLConditionClause(Context context, long keyword, Expression condition) {
        super(context);

        this.keyword = checkIsKeyword(keyword);
        this.condition = makeSingle(condition);
    }

    public final long getKeyword() {
        return keyword;
    }

    public final Expression getCondition() {
        return condition.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(condition, recurseMode, iterator);
    }
}
