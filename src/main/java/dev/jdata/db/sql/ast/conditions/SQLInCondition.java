package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public final class SQLInCondition extends BaseSQLInCondition {

    public SQLInCondition(Context context, long inKeyword, IListGetters<Expression> expressions) {
        super(context, inKeyword, expressions);
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onIn(this, parameter);
    }
}
