package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public final class SQLSelectStatement extends BaseSQLSelectStatement {

    public SQLSelectStatement(Context context, IListGetters<SQLSelectStatementPart> parts, IListGetters<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
