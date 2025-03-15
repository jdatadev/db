package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;

abstract class SQLTriggerStatement extends BaseSQLDDLOperationStatement {

    SQLTriggerStatement(Context context, long operationKeyword, long triggerKeyword, long name) {
        super(context, operationKeyword, triggerKeyword, name);
    }
}
