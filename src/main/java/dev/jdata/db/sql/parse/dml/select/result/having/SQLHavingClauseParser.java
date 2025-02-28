package dev.jdata.db.sql.parse.dml.select.result.having;

import java.io.IOException;
import java.util.Objects;

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

    public final SQLHavingClause parseHavingClause(SQLExpressionLexer lexer) throws ParserException, IOException {

        Objects.requireNonNull(lexer);

        return parseConditionClause(lexer, SQLToken.HAVING, SQLHavingClause::new);
    }
}
