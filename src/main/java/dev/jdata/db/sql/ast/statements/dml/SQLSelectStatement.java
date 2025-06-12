package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLSelectStatement extends BaseSQLSelectStatement {

    public SQLSelectStatement(Context context, IIndexListGetters<SQLSelectStatementPart> parts, IIndexListGetters<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
