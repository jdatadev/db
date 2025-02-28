package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.parse.context.Context;

public final class SQLTableName extends SQLObjectName {

    public SQLTableName(Context context, long name) {
        super(context, name);
    }
}
