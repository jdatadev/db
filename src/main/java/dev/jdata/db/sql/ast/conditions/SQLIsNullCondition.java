package dev.jdata.db.sql.ast.conditions;

import org.jutils.parse.context.Context;

public final class SQLIsNullCondition extends BaseSQLIsNullCondition {

    public SQLIsNullCondition(Context context, long isKeyword, long nullKeyword) {
        super(context, isKeyword, nullKeyword);
    }

    @Override
    public <P, R> R visit(SQLConditionVisitor<P, R> visitor, P parameter) {

        return visitor.onIsNull(this, parameter);
    }
}
