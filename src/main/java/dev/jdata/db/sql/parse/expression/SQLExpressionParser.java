package dev.jdata.db.sql.parse.expression;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.operator.Arithmetic;
import org.jutils.ast.operator.Operator;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.expressions.BaseSQLFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLAggregateFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLDecimalLiteral;
import dev.jdata.db.sql.ast.expressions.SQLFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLIntegerLiteral;
import dev.jdata.db.sql.ast.expressions.SQLLargeIntegerLiteral;
import dev.jdata.db.sql.ast.expressions.SQLLiteral;
import dev.jdata.db.sql.ast.expressions.SQLStringLiteral;
import dev.jdata.db.sql.ast.expressions.SQLSubSelectExpression;
import dev.jdata.db.sql.ast.expressions.SQLSubSelectStatement;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.utils.adt.decimals.MutableDecimal;

public final class SQLExpressionParser extends BaseSQLExpressionParser {

    private SQLSubSelectParser subSelectParser;

    public final void initialize(SQLSubSelectParser subSelectParser) {

        this.subSelectParser = Objects.requireNonNull(subSelectParser);
    }

    public Expression parseExpression(SQLExpressionLexer lexer)
            throws ParserException, IOException {

        return parseExpressionList(lexer);
    }

    private Expression parseExpressionList(SQLExpressionLexer lexer) throws ParserException, IOException {

        return parseExpressionList(lexer, this::parseExpressionPart, SQLExpressionParser::parseArithmeticOperator);
    }

    private <T extends Operator> Expression parseExpressionPart(SQLExpressionLexer lexer) throws ParserException, IOException {

        return parseExpressionPart(lexer, SQLExpressionParser::parseArithmeticOperator);
    }

    private static final SQLToken[] EXPRESSION_TOKENS = new SQLToken[] {

            SQLToken.STRING_LITERAL,
            SQLToken.INTEGER_NUMBER,
            SQLToken.PLUS,
            SQLToken.MINUS,

            SQLToken.MIN,
            SQLToken.MAX,
            SQLToken.COUNT,
            SQLToken.AVG,
            SQLToken.SUM,

            SQLToken.NAME
    };

    private <T extends Operator> Expression parseExpressionPart(SQLExpressionLexer lexer, OperatorParser<T> operatorParser) throws ParserException, IOException {

        final SQLToken[] expectedTokens = EXPRESSION_TOKENS;

        final SQLAllocator allocator = lexer.getAllocator();

        final StringResolver stringResolver = lexer.getStringResolver();
        final SQLScratchExpressionValues scratchExpressionValues = lexer.getScratchExpressionValues();

        final Expression result;

        switch (lexer.lex(expectedTokens)) {

        case STRING_LITERAL:

            result = new SQLStringLiteral(makeContext(), lexer.getStringRef(1, 1));
            break;

        case INTEGER_NUMBER:

            result = parseIntegerOrDecimal(lexer, allocator, stringResolver, scratchExpressionValues.getIntegerValue(), scratchExpressionValues.getFractionValue());
            break;

        case PLUS:

            lexer.lexExpect(SQLToken.INTEGER_NUMBER);

            result = parseIntegerOrDecimal(lexer, allocator, stringResolver, scratchExpressionValues, false);
            break;

        case MINUS:

            lexer.lexExpect(SQLToken.INTEGER_NUMBER);

            result = parseIntegerOrDecimal(lexer, allocator, stringResolver, scratchExpressionValues, true);
            break;

        case LPAREN:

            result = parseNestedExpression(lexer, operatorParser);
            break;

        case MIN:
        case MAX:
        case COUNT:
        case AVG:
        case SUM:

            result = parseFunction(lexer, lexer.getStringRef(), SQLAggregateFunctionCallExpression::new);
            break;

        case NAME:

            result = parseFunction(lexer, lexer.getStringRef(), SQLFunctionCallExpression::new);
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @FunctionalInterface
    private interface FunctionFactory<T extends BaseSQLFunctionCallExpression> {

        T createFunction(Context context, long functionName, List<Expression> parameters);
    }

    private <T extends BaseSQLFunctionCallExpression> T parseFunction(SQLExpressionLexer lexer, long functionName, FunctionFactory<T> functionFactory)
            throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

        final List<Expression> parameters = allocator.allocateList(10);

        try {
            parseParameters(lexer, parameters);
        }
        finally {

            allocator.freeList(parameters);
        }

        return functionFactory.createFunction(makeContext(), functionName, parameters);
    }

    private void parseParameters(SQLExpressionLexer lexer, List<Expression> dst) throws ParserException, IOException {

        lexer.lexExpect(SQLToken.LPAREN);

        for (;;) {

            final Expression expression = parseExpression(lexer);

            dst.add(expression);

            if (!lexer.lex(SQLToken.COMMA)) {

                break;
            }
        }

        lexer.lexExpect(SQLToken.RPAREN);
    }

    private <T extends Operator> Expression parseNestedExpression(SQLExpressionLexer lexer, OperatorParser<T> operatorParser) throws ParserException, IOException {

        final Expression result;

        if (lexer.peek(SQLToken.SELECT)) {

            final long selectKeyword = lexer.getStringRef();

            result = parseSubSelectExpression(lexer, selectKeyword);
        }
        else {
            result = parseExpressionList(lexer);
        }

        lexer.lexExpect(SQLToken.RPAREN);

        return result;
    }

    private SQLSubSelectExpression parseSubSelectExpression(SQLExpressionLexer lexer, long selectKeyword) throws ParserException, IOException {

        final SQLSubSelectStatement subSelectStatement = subSelectParser.parseSelect(lexer, selectKeyword);

        return new SQLSubSelectExpression(makeContext(), subSelectStatement);
    }

    private static SQLLiteral parseIntegerOrDecimal(SQLLexer lexer, SQLAllocator allocator, StringResolver stringResolver, SQLScratchExpressionValues scratchExpressionValues,
            boolean isNegative) throws ParserException, IOException {

        final SQLScratchIntegerValue scratchIntegerValue = scratchExpressionValues.getIntegerValue();

        parseInteger(lexer, stringResolver, scratchIntegerValue, isNegative);

        return parseIntegerOrDecimal(lexer, allocator, stringResolver, scratchIntegerValue, scratchExpressionValues.getFractionValue());
    }

    private static SQLLiteral parseIntegerOrDecimal(SQLLexer lexer, SQLAllocator allocator, StringResolver stringResolver, SQLScratchIntegerValue scratchIntegerValue,
            SQLScratchIntegerValue scratchFractionValue) throws ParserException, IOException {

        final SQLLiteral result;

        final boolean isLargeInteger = scratchIntegerValue.isLargeIntegerSet();

        if (lexer.peek(SQLToken.PERIOD)) {

            lexer.lexSkip(SQLToken.PERIOD);

            lexer.lexExpect(SQLToken.INTEGER_NUMBER);

            parseInteger(lexer, stringResolver, scratchFractionValue, false);

            final boolean isLargeFraction = scratchFractionValue.isLargeIntegerSet();

            final MutableDecimal decimal;

            if (isLargeInteger && isLargeFraction) {

                decimal = allocator.allocateDecimal(scratchIntegerValue.getLargeInteger(), scratchFractionValue.getLargeInteger());
            }
            else if (isLargeInteger) {

                decimal = allocator.allocateDecimal(scratchIntegerValue.getLargeInteger(), scratchFractionValue.getLongInteger());
            }
            else if (isLargeFraction) {

                decimal = allocator.allocateDecimal(scratchIntegerValue.getLongInteger(), scratchFractionValue.getLargeInteger());
            }
            else {
                decimal = allocator.allocateDecimal(scratchIntegerValue.getLongInteger(), scratchFractionValue.getLongInteger());
            }

            result = new SQLDecimalLiteral(makeContext(), decimal);
        }
        else {
            result = isLargeInteger
                    ? new SQLLargeIntegerLiteral(makeContext(), scratchIntegerValue.getLargeInteger())
                    : new SQLIntegerLiteral(makeContext(), scratchIntegerValue.getLongInteger());
        }

        return result;
    }

    private static final int MAX_NEGATIVE_LONG_DIGITS = String.valueOf(Long.MIN_VALUE).length();
    private static final int MAX_POSITIVE_LONG_DIGITS = String.valueOf(Long.MAX_VALUE).length();

    private static final int MIN_LONG_DIGITS = Math.min(MAX_NEGATIVE_LONG_DIGITS, MAX_POSITIVE_LONG_DIGITS);

    private static void parseInteger(SQLLexer lexer, StringResolver stringResolver, SQLScratchIntegerValue scratchIntegerValue, boolean isNegative) {

        final long stringRef = lexer.getStringRef();

        final int minLongDigits = MIN_LONG_DIGITS;

        final long tokenLength = lexer.getTokenLength();

        scratchIntegerValue.clear();

        if (tokenLength <= minLongDigits) {

            final long longValue = stringResolver.asLong(stringRef);

            scratchIntegerValue.setLongInteger(isNegative ? longValue : -longValue);
        }
        else {
            final StringBuilder sb = scratchIntegerValue.getLargeIntegerStringBuilder();

            stringResolver.appendString(stringRef, sb);

            scratchIntegerValue.setLargeInteger(sb);
        }
    }

    private static final SQLToken[] ARITHMETIC_OPERATOR_TOKENS = new SQLToken[] {

            SQLToken.PLUS,
            SQLToken.MINUS,
            SQLToken.MULTIPLY,
            SQLToken.DIVIDE,
            SQLToken.MODULUS
    };

    private static Arithmetic parseArithmeticOperator(SQLLexer lexer, StringResolver stringResolver) throws ParserException, IOException {

        final SQLToken[] expectedTokens = ARITHMETIC_OPERATOR_TOKENS;

        final Arithmetic result;

        switch (lexer.lex(expectedTokens)) {

        case PLUS:

            result = Arithmetic.PLUS;
            break;

        case MINUS:

            result = Arithmetic.MINUS;
            break;

        case MULTIPLY:

            result = Arithmetic.MULTIPLY;
            break;

        case DIVIDE:

            result = Arithmetic.DIVIDE;
            break;

        case MODULUS:

            result = Arithmetic.MODULUS;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }
}
