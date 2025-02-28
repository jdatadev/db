package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

abstract class BaseSQLIsNullCondition extends BaseSQLCondition {

    private final long isKeyword;
    private final long nullKeyword;

    BaseSQLIsNullCondition(Context context, long isKeyword, long nullKeyword) {
        super(context);

        this.isKeyword = checkIsKeyword(isKeyword);
        this.nullKeyword = checkIsKeyword(nullKeyword);
    }

    public final long getIsKeyword() {
        return isKeyword;
    }

    public final long getNullKeyword() {
        return nullKeyword;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
