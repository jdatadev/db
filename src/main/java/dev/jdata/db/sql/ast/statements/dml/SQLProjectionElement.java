package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLProjectionElement extends BaseSQLElement {

    public abstract <T, R> R visit(SQLProjectionElementVisitor<T, R> visitor, T parameter);

    SQLProjectionElement(Context context) {
        super(context);
    }
}
