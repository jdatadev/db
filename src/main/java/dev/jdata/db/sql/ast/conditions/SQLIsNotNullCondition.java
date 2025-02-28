package dev.jdata.db.sql.ast.conditions;

import org.jutils.parse.context.Context;

public final class SQLIsNotNullCondition extends BaseSQLIsNullCondition {

    private final long notKeyword;

    public SQLIsNotNullCondition(Context context, long isKeyword, long notKeyword, long nullKeyword) {
        super(context, isKeyword, nullKeyword);

        this.notKeyword = checkIsKeyword(notKeyword);
    }

    public long getNotKeyword() {
        return notKeyword;
    }

    @Override
    public <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter) {

        return visitor.onIsNotNull(this, parameter);
    }
}
