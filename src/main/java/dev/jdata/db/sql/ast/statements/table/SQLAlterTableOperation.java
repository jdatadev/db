package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLAlterTableOperation extends BaseSQLElement {

    public abstract <T, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<T, R, E> visitor, T parameter) throws E;

    SQLAlterTableOperation(Context context) {
        super(context);
    }
}
