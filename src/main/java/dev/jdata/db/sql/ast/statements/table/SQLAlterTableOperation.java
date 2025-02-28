package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLAlterTableOperation extends BaseSQLElement {

    public abstract <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter);

    SQLAlterTableOperation(Context context) {
        super(context);
    }
}
