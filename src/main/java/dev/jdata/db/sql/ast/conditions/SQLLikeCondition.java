package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

public final class SQLLikeCondition extends BaseSQLLikeCondition {

    public SQLLikeCondition(Context context, long likeKeyword, Expression expression) {
        super(context, likeKeyword, expression);
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onLike(this, parameter);
    }
}
