package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLNewTriggerCorrelation extends SQLTriggerCorrelation {

    public SQLNewTriggerCorrelation(Context context, long newKeyword, long asKeyword, long correlation) {
        super(context, newKeyword, asKeyword, correlation);
    }

    public final long getNewKeyword() {

        return getKeyword();
    }
}
