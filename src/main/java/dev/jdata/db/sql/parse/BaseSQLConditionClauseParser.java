package dev.jdata.db.sql.parse;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLConditionClause;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;

public abstract class BaseSQLConditionClauseParser extends BaseSQLParser {

    @FunctionalInterface
    protected interface ConditionClauseFactory<T extends SQLConditionClause> {

        T createConditionClause(Context context, long keyword, Expression condition);
    }

    private final SQLConditionParser conditionParser;

    protected BaseSQLConditionClauseParser(SQLConditionParser conditionParser) {

        this.conditionParser = Objects.requireNonNull(conditionParser);
    }

    protected final <T extends SQLConditionClause, E extends Exception, I extends CharInput<E>> T parseConditionClause(SQLExpressionLexer<E, I> lexer, SQLToken keywordToken,
            ConditionClauseFactory<T> conditionClauseFactory) throws ParserException, E {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(keywordToken);
        Objects.requireNonNull(conditionClauseFactory);

        final T result;

        if (lexer.peek(keywordToken)) {

            final long keyword = lexer.lexKeyword(keywordToken);

            final Expression condition = conditionParser.parseCondition(lexer);

            result = conditionClauseFactory.createConditionClause(makeContext(), keyword, condition);
        }
        else {
            result = null;
        }

        return result;
    }
}
