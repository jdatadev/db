package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLNotInCondition extends BaseSQLInCondition {

    private final long notKeyword;

    public SQLNotInCondition(Context context, long notKeyword, long inKeyword, IIndexListGetters<Expression> expressions) {
        super(context, inKeyword, expressions);

        this.notKeyword = checkIsKeyword(notKeyword);
    }

    public long getNotKeyword() {
        return notKeyword;
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onNotIn(this, parameter);
    }
}
