package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLStatement;

abstract class SQLDMLStatement extends BaseSQLStatement {

    SQLDMLStatement(Context context) {
        super(context);
    }
}
