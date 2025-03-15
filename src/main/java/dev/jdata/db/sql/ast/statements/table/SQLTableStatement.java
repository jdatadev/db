package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;

abstract class SQLTableStatement extends BaseSQLDDLOperationStatement {

    SQLTableStatement(Context context, long operationKeyword, long tableKeyword, long name) {
        super(context, operationKeyword, tableKeyword, name);
    }
}
