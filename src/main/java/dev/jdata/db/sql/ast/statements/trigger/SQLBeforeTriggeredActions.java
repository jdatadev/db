package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public final class SQLBeforeTriggeredActions extends SQLTriggeredActions {

    private final long beforeKeyword;

    public SQLBeforeTriggeredActions(Context context, long beforeKeyword, IListGetters<SQLTriggeredAction> actions) {
        super(context, actions);

        this.beforeKeyword = checkIsKeyword(beforeKeyword);
    }

    public long getBeforeKeyword() {
        return beforeKeyword;
    }
}
