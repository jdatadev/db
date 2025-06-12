package dev.jdata.db.sql.parse.dml.select.result.having;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.dml.SQLHavingClause;
import dev.jdata.db.sql.parse.BaseSQLConditionClauseParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;

public class SQLHavingClauseParser extends BaseSQLConditionClauseParser {

    public SQLHavingClauseParser(SQLConditionParser conditionParser) {
        super(conditionParser);
    }

    public final <E extends Exception, I extends CharInput<E>> SQLHavingClause parseHavingClause(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        Objects.requireNonNull(lexer);

        return parseConditionClause(lexer, SQLToken.HAVING, SQLHavingClause::new);
    }
}
