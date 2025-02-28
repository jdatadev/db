package dev.jdata.db.sql.parse.expression;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLSubSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatementPart;
import dev.jdata.db.sql.ast.statements.dml.SQLUnion;
import dev.jdata.db.sql.parse.dml.select.BaseSQLSelectParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public final class SQLSubSelectParser extends BaseSQLSelectParser<SQLSubSelectStatement> {

    public SQLSubSelectParser(SQLExpressionParser expressionParser, SQLConditionParser conditionParser, SQLWhereClauseParser whereClauseParser) {
        super(expressionParser, conditionParser, whereClauseParser);
    }

    @Override
    protected SQLSubSelectStatement createSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions) {

        return new SQLSubSelectStatement(context, parts, unions);
    }
}
