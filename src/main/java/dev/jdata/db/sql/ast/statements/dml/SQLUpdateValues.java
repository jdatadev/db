package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLUpdateValues extends BaseSQLElement {

    SQLUpdateValues(Context context) {
        super(context);
    }
}
