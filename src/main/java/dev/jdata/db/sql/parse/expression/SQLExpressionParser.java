package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.ast.operator.Arithmetic;
import org.jutils.ast.operator.Operator;
import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.ISQLAllocator;
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

    public <E extends Exception, I extends CharInput<E>> Expression parseExpression(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        return parseExpressionList(lexer);
    }

    private <E extends Exception, I extends CharInput<E>> Expression parseExpressionList(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        return parseExpressionList(lexer, this::parseExpressionPart, SQLExpressionParser::parseArithmeticOperator);
    }

    private <T extends Operator, E extends Exception, I extends CharInput<E>> Expression parseExpressionPart(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

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

    private <T extends Operator, E extends Exception, I extends CharInput<E>> Expression parseExpressionPart(SQLExpressionLexer<E, I> lexer,
            OperatorParser<T, E, I> operatorParser) throws ParserException, E {

        final SQLToken[] expectedTokens = EXPRESSION_TOKENS;

        final ISQLAllocator allocator = lexer.getAllocator();

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

        T createFunction(Context context, long functionName, IIndexListGetters<Expression> parameters);
    }

    private <T extends BaseSQLFunctionCallExpression, E extends Exception, I extends CharInput<E>> T parseFunction(SQLExpressionLexer<E, I> lexer, long functionName,
            FunctionFactory<T> functionFactory) throws ParserException, E {

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<Expression> parameters = allocator.allocateList(10);

        try {
            parseParameters(lexer, parameters);
        }
        finally {

            allocator.freeList(parameters);
        }

        return functionFactory.createFunction(makeContext(), functionName, parameters);
    }

    private <E extends Exception, I extends CharInput<E>> void parseParameters(SQLExpressionLexer<E, I> lexer, IAddableList<Expression> dst) throws ParserException, E {

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

    private <T extends Operator, E extends Exception, I extends CharInput<E>> Expression parseNestedExpression(SQLExpressionLexer<E, I> lexer,
            OperatorParser<T, E, I> operatorParser) throws ParserException, E {

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

    private <E extends Exception, I extends CharInput<E>> SQLSubSelectExpression parseSubSelectExpression(SQLExpressionLexer<E, I> lexer, long selectKeyword)
            throws ParserException, E {

        final SQLSubSelectStatement subSelectStatement = subSelectParser.parseSelect(lexer, selectKeyword);

        return new SQLSubSelectExpression(makeContext(), subSelectStatement);
    }

    private static <E extends Exception, I extends CharInput<E>> SQLLiteral parseIntegerOrDecimal(SQLLexer<E, I> lexer, ISQLAllocator allocator, StringResolver stringResolver,
            SQLScratchExpressionValues scratchExpressionValues, boolean isNegative) throws ParserException, E {

        final SQLScratchIntegerValue scratchIntegerValue = scratchExpressionValues.getIntegerValue();

        parseInteger(lexer, stringResolver, scratchIntegerValue, isNegative);

        return parseIntegerOrDecimal(lexer, allocator, stringResolver, scratchIntegerValue, scratchExpressionValues.getFractionValue());
    }

    private static <E extends Exception, I extends CharInput<E>> SQLLiteral parseIntegerOrDecimal(SQLLexer<E, I> lexer, ISQLAllocator allocator, StringResolver stringResolver,
            SQLScratchIntegerValue scratchIntegerValue, SQLScratchIntegerValue scratchFractionValue) throws ParserException, E {

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

    private static <E extends Exception, I extends CharInput<E>> void parseInteger(SQLLexer<E, I> lexer, StringResolver stringResolver,
            SQLScratchIntegerValue scratchIntegerValue, boolean isNegative) {

        final long stringRef = lexer.getStringRef();

        final int minLongDigits = MIN_LONG_DIGITS;

        final long tokenLength = lexer.getTokenLength();

        scratchIntegerValue.clear();

        if (tokenLength <= minLongDigits) {

            final long longValue = stringResolver.asUnsignedLong(stringRef);

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

    private static <E extends Exception, I extends CharInput<E>> Arithmetic parseArithmeticOperator(SQLLexer<E, I> lexer, StringResolver stringResolver)
            throws ParserException, E {

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
