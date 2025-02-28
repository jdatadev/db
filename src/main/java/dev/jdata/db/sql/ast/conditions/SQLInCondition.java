package dev.jdata.db.sql.ast.conditions;

import java.util.List;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

public final class SQLInCondition extends BaseSQLInCondition {

    public SQLInCondition(Context context, long inKeyword, List<Expression> expressions) {
        super(context, inKeyword, expressions);
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onIn(this, parameter);
    }
}
