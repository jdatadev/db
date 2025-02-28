package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLTableName;

public class SQLDeleteTriggerEvent extends SQLTriggerEvent {

    public SQLDeleteTriggerEvent(Context context, long deleteKeyword, long onKeyword, SQLTableName tableName) {
        super(context, deleteKeyword, onKeyword, tableName);
    }
}
