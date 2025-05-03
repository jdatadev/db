package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.BaseSQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatementPart;
import dev.jdata.db.sql.ast.statements.dml.SQLUnion;

public final class SQLSubSelectStatement extends BaseSQLSelectStatement {

    public SQLSubSelectStatement(Context context, IListGetters<SQLSelectStatementPart> parts, IListGetters<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
