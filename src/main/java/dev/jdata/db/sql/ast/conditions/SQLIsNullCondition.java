package dev.jdata.db.sql.ast.conditions;

import org.jutils.parse.context.Context;

public final class SQLIsNullCondition extends BaseSQLIsNullCondition {

    public SQLIsNullCondition(Context context, long isKeyword, long nullKeyword) {
        super(context, isKeyword, nullKeyword);
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onIsNull(this, parameter);
    }
}
