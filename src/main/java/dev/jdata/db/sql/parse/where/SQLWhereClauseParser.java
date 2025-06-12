package dev.jdata.db.sql.parse.where;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
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

    public <E extends Exception, I extends CharInput<E>> SQLWhereClause parseWhereClause(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        Objects.requireNonNull(lexer);

        return parseConditionClause(lexer, SQLToken.WHERE, SQLWhereClause::new);
    }
}
