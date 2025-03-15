package dev.jdata.db.sql.ast.statements.function;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;

abstract class SQLFunctionStatement extends BaseSQLDDLOperationStatement {

    SQLFunctionStatement(Context context, long operationKeyword, long functionKeyword, long name) {
        super(context, operationKeyword, functionKeyword, name);
    }
}
