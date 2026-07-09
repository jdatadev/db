package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLProjectionElement extends BaseSQLElement {

    public abstract <P, R> R visit(SQLProjectionElementVisitor<P, R> visitor, P parameter);

    SQLProjectionElement(Context context) {
        super(context);
    }
}
