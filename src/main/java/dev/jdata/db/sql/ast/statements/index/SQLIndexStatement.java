package dev.jdata.db.sql.ast.statements.index;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLOperationStatement;

abstract class SQLIndexStatement extends BaseSQLOperationStatement {

    SQLIndexStatement(Context context, long operationKeyword, long indexKeyword, long name) {
        super(context, operationKeyword, indexKeyword, name);
    }
}
