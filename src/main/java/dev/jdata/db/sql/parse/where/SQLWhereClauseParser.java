package dev.jdata.db.sql.parse.where;

import java.io.IOException;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.parse.BaseSQLConditionClauseParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;

public final class SQLWhereClauseParser extends BaseSQLConditionClauseParser {

    public SQLWhereClauseParser(SQLConditionParser conditionParser) {
        super(conditionParser);
    }

    public SQLWhereClause parseWhereClause(SQLExpressionLexer lexer) throws ParserException, IOException {

        Objects.requireNonNull(lexer);

        return parseConditionClause(lexer, SQLToken.WHERE, SQLWhereClause::new);
    }
}
