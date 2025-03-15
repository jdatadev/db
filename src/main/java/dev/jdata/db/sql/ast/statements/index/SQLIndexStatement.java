package dev.jdata.db.sql.ast.statements.index;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;

abstract class SQLIndexStatement extends BaseSQLDDLOperationStatement {

    SQLIndexStatement(Context context, long operationKeyword, long indexKeyword, long name) {
        super(context, operationKeyword, indexKeyword, name);
    }
}
