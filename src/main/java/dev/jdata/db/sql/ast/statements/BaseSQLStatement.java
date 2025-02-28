package dev.jdata.db.sql.ast.statements;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class BaseSQLStatement extends BaseSQLElement {

    public abstract <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter);

    protected BaseSQLStatement(Context context) {
        super(context);
    }
}
