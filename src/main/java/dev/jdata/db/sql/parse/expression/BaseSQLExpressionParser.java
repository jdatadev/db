package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.operator.Operator;
import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;

abstract class BaseSQLExpressionParser extends BaseSQLParser {

    @FunctionalInterface
    interface ExpressionPartParser<E extends Exception, I extends CharInput<E>> {

        Expression parseExpressionPart(SQLExpressionLexer<E, I> lexer) throws ParserException, E;
    }

    @FunctionalInterface
    interface OperatorParser<T extends Operator, E extends Exception, I extends CharInput<E>> {

        T parseOperator(SQLLexer<E, I> lexer, StringResolver stringResolver) throws ParserException, E;
    }

    static <T extends Operator, E extends Exception, I extends CharInput<E>> Expression parseExpressionList(SQLExpressionLexer<E, I> lexer,
            ExpressionPartParser<E, I> expressionPartParser, OperatorParser<T, E, I> operatorParser) throws ParserException, E {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(expressionPartParser);
        Objects.requireNonNull(operatorParser);

        final Expression result;

        final int initialCapacity = 10;

        final StringResolver stringResolver = lexer.getStringResolver();

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<Expression> expressions = allocator.allocateList(initialCapacity);
        final IAddableList<Operator> operators = allocator.allocateList(initialCapacity - 1);

        try {
            boolean done = false;

            do {
                final Expression expression = expressionPartParser.parseExpressionPart(lexer);

                expressions.add(expression);

                final T operator = operatorParser.parseOperator(lexer, stringResolver);

                if (operator != null) {

                    operators.add(operator);
                }
                else {
                    done = true;
                }

            } while (!done);

            result = expressions.getNumElements() == 1
                    ? expressions.get(0)
                    : new ExpressionList(makeContext(), operators, expressions);
        }
        finally {

            allocator.freeList(expressions);
        }

        return result;
    }
}
