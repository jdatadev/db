package dev.jdata.db.sql.ast.clauses;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.context.Context;

public final class SQLWhereClause extends SQLConditionClause {

    public SQLWhereClause(Context context, long keyword, Expression condition) {
        super(context, keyword, condition);
    }
}
