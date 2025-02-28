package dev.jdata.db.sql.parse.dml.select;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatementPart;
import dev.jdata.db.sql.ast.statements.dml.SQLUnion;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public class SQLSelectParser extends BaseSQLSelectParser<SQLSelectStatement> {

    public SQLSelectParser(SQLExpressionParser expressionParser, SQLConditionParser conditionParser, SQLWhereClauseParser whereClauseParser) {
        super(expressionParser, conditionParser, whereClauseParser);
    }

    @Override
    protected SQLSelectStatement createSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions) {

        return new SQLSelectStatement(context, parts, unions);
    }
}
