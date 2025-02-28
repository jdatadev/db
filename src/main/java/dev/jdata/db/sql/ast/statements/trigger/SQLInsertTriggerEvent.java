package dev.jdata.db.sql.ast.statements.trigger;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLTableName;

public class SQLInsertTriggerEvent extends SQLTriggerEvent {

    public SQLInsertTriggerEvent(Context context, long dmlStatementKeyword, long onKeyword, SQLTableName tableName) {
        super(context, dmlStatementKeyword, onKeyword, tableName);
    }
}
