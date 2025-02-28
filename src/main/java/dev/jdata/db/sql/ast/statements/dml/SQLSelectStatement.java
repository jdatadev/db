package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.parse.context.Context;

public final class SQLSelectStatement extends BaseSQLSelectStatement {

    public SQLSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
