package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

abstract class SQLColumnOperation extends SQLAlterTableOperation {

    SQLColumnOperation(Context context) {
        super(context);
    }
}
