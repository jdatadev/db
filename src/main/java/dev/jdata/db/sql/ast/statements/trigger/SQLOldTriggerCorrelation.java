package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

public final class SQLOldTriggerCorrelation extends SQLTriggerCorrelation {

    public SQLOldTriggerCorrelation(Context context, long oldKeyword, long asKeyword, long correlation) {
        super(context, oldKeyword, asKeyword, correlation);
    }

    public final long getOldKeyword() {

        return getKeyword();
    }
}
