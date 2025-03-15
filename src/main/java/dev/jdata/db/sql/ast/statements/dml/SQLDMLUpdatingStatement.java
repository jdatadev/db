package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

public abstract class SQLDMLUpdatingStatement extends SQLDMLStatement {

    SQLDMLUpdatingStatement(Context context) {
        super(context);
    }
}
