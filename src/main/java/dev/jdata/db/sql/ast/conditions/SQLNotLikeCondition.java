package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

public final class SQLNotLikeCondition extends BaseSQLLikeCondition {

    private final long notKeyword;

    public SQLNotLikeCondition(Context context, long notKeyword, long likeKeyword, Expression expression) {
        super(context, likeKeyword, expression);

        this.notKeyword = checkIsKeyword(notKeyword);
    }

    public long getNotKeyword() {
        return notKeyword;
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onNotLike(this, parameter);
    }
}
