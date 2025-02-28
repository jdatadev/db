package dev.jdata.db.sql.ast.clauses;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLClause extends BaseSQLElement {

    protected SQLClause(Context context) {
        super(context);
    }
}
