package dev.jdata.db.sql.ast.statements.trigger;

import java.util.List;

import org.jutils.parse.context.Context;

public final class SQLForEachRowTriggeredActions extends SQLTriggeredActions {

    private final long forKeyword;
    private final long eachKeyword;
    private final long rowKeyword;

    public SQLForEachRowTriggeredActions(Context context, long forKeyword, long eachKeyword, long rowKeyword, List<SQLTriggeredAction> actions) {
        super(context, actions);

        this.forKeyword = checkIsKeyword(forKeyword);
        this.eachKeyword = checkIsKeyword(eachKeyword);
        this.rowKeyword = checkIsKeyword(rowKeyword);
    }

    public long getForKeyword() {
        return forKeyword;
    }

    public long getEachKeyword() {
        return eachKeyword;
    }

    public long getRowKeyword() {
        return rowKeyword;
    }
}
