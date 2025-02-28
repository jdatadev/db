package dev.jdata.db.sql.ast.statements.trigger;

import java.util.List;

import org.jutils.parse.context.Context;

public final class SQLAfterTriggeredActions extends SQLTriggeredActions {

    private final long afterKeyword;

    public SQLAfterTriggeredActions(Context context, long afterKeyword, List<SQLTriggeredAction> actions) {
        super(context, actions);

        this.afterKeyword = checkIsKeyword(afterKeyword);
    }

    public long getAfterKeyword() {
        return afterKeyword;
    }
}
