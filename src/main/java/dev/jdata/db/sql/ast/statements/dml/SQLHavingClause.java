package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLConditionClause;

public final class SQLHavingClause extends SQLConditionClause {

    public SQLHavingClause(Context context, long keyword, Expression condition) {
        super(context, keyword, condition);
    }
}
