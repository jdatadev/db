package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLOperationStatement;

abstract class SQLTriggerStatement extends BaseSQLOperationStatement {

    SQLTriggerStatement(Context context, long operationKeyword, long triggerKeyword, long name) {
        super(context, operationKeyword, triggerKeyword, name);
    }
}
