package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLAlterTableOperation extends BaseSQLElement {

    public abstract <P, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<P, R, E> visitor, P parameter) throws E;

    SQLAlterTableOperation(Context context) {
        super(context);
    }
}
