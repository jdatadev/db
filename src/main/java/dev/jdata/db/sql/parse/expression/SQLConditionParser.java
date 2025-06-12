package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.operator.Logical;
import org.jutils.ast.operator.Relational;
import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLToken;

public final class SQLConditionParser extends BaseSQLExpressionParser {

    private final SQLExpressionParser expressionParser;

    public SQLConditionParser(SQLExpressionParser expressionParser) {

        this.expressionParser = Objects.requireNonNull(expressionParser);
    }

    public <E extends Exception, I extends CharInput<E>> Expression parseCondition(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        return parseConditionList(lexer);
    }

    private <E extends Exception, I extends CharInput<E>> Expression parseConditionList(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        return parseExpressionList(lexer, this::parseRelationalListOrNestedCondition, SQLConditionParser::parseLogicalOperator);
    }

    private <E extends Exception, I extends CharInput<E>> Expression parseRelationalListOrNestedCondition(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        final Expression result;

        if (lexer.lex(SQLToken.LPAREN)) {

            result = parseConditionList(lexer);

            lexer.lexExpect(SQLToken.RPAREN);
        }
        else {
            result = parseRelationalList(lexer);
        }

        return result;
    }

    private <E extends Exception, I extends CharInput<E>> Expression parseRelationalList(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        final Expression expression = parseExpressionList(lexer, expressionParser::parseExpression, SQLConditionParser::parseRelationalOperator);

        if ((!(expression instanceof ExpressionList)) || ((ExpressionList)expression).getExpressions().size() != 2) {

            throw new ParserException("Expected two parts in expression list");
        }

        return expression;
    }

    private static final SQLToken[] LOGICAL_OPERATOR_TOKENS = new SQLToken[] {

            SQLToken.AND,
            SQLToken.OR
    };

    private static <E extends Exception, I extends CharInput<E>> Logical parseLogicalOperator(SQLLexer<E, I> lexer, StringResolver stringResolver) throws ParserException, E {

        final SQLToken[] expectedTokens = LOGICAL_OPERATOR_TOKENS;

        final Logical result;

        switch (lexer.lex(expectedTokens)) {

        case AND:

            result = Logical.AND;
            break;

        case OR:

            result = Logical.OR;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    private static final SQLToken[] RELATIONAL_OPERATOR_TOKENS = new SQLToken[] {

            SQLToken.LT,
            SQLToken.LTE,
            SQLToken.EQ,
            SQLToken.GT,
            SQLToken.GTE
    };

    private static <E extends Exception, I extends CharInput<E>> Relational parseRelationalOperator(SQLLexer<E, I> lexer, StringResolver stringResolver)
            throws ParserException, E {

        final SQLToken[] expectedTokens = RELATIONAL_OPERATOR_TOKENS;

        final Relational result;

        switch (lexer.lex(expectedTokens)) {

        case LT:

            result = Relational.LESS_THAN;
            break;

        case LTE:

            result = Relational.LESS_THAN_OR_EQUALS;
            break;

        case EQ:

            result = Relational.EQUALS;
            break;

        case GT:

            result = Relational.GREATER_THAN;
            break;

        case GTE:

            result = Relational.GREATER_THAN_OR_EQUALS;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }
}
