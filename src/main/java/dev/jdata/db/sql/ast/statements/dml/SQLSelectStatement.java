package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.list.IIndexListView;
import org.jutils.parse.context.Context;

public final class SQLSelectStatement extends BaseSQLSelectStatement {

    public SQLSelectStatement(Context context, IIndexListView<SQLSelectStatementPart> parts, IIndexListView<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
