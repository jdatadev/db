package dev.jdata.db.sql.ast.expressions;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.BaseSQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatementPart;
import dev.jdata.db.sql.ast.statements.dml.SQLUnion;

public final class SQLSubSelectStatement extends BaseSQLSelectStatement {

    public SQLSubSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions) {
        super(context, parts, unions);
    }
}
