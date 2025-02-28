package dev.jdata.db.sql.parse.expression;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.operator.Logical;
import org.jutils.ast.operator.Relational;
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

    public Expression parseCondition(SQLExpressionLexer lexer) throws ParserException, IOException {

        return parseConditionList(lexer);
    }

    private Expression parseConditionList(SQLExpressionLexer lexer) throws ParserException, IOException {

        return parseExpressionList(lexer, this::parseRelationalListOrNestedCondition, SQLConditionParser::parseLogicalOperator);
    }

    private Expression parseRelationalListOrNestedCondition(SQLExpressionLexer lexer) throws ParserException, IOException {

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

    private Expression parseRelationalList(SQLExpressionLexer lexer) throws ParserException, IOException {

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

    private static Logical parseLogicalOperator(SQLLexer lexer, StringResolver stringResolver) throws ParserException, IOException {

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

    private static Relational parseRelationalOperator(SQLLexer lexer, StringResolver stringResolver) throws ParserException, IOException {

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
