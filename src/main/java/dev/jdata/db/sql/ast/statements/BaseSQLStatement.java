package dev.jdata.db.sql.ast.statements;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class BaseSQLStatement extends BaseSQLElement {

    public abstract <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E;

    protected BaseSQLStatement(Context context) {
        super(context);
    }
}
