package dev.jdata.db.sql.ast.expressions;

import org.jutils.parse.context.Context;

public abstract class SQLLiteral extends BaseSQLExpression {

    SQLLiteral(Context context) {
        super(context);
    }
}
