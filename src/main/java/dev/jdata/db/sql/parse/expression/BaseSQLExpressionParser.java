package dev.jdata.db.sql.parse.expression;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.operator.Operator;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;

abstract class BaseSQLExpressionParser extends BaseSQLParser {

    @FunctionalInterface
    interface ExpressionPartParser {

        Expression parseExpressionPart(SQLExpressionLexer lexer) throws ParserException, IOException;
    }

    @FunctionalInterface
    interface OperatorParser<T extends Operator> {

        T parseOperator(SQLLexer lexer, StringResolver stringResolver) throws ParserException, IOException;
    }

    static <T extends Operator> Expression parseExpressionList(SQLExpressionLexer lexer, ExpressionPartParser expressionPartParser, OperatorParser<T> operatorParser)
            throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(expressionPartParser);
        Objects.requireNonNull(operatorParser);

        final Expression result;

        final int initialCapacity = 10;

        final StringResolver stringResolver = lexer.getStringResolver();

        final SQLAllocator allocator = lexer.getAllocator();

        final List<Expression> expressions = allocator.allocateList(initialCapacity);
        final List<Operator> operators = allocator.allocateList(initialCapacity - 1);

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

            result = expressions.size() == 1
                    ? expressions.get(0)
                    : new ExpressionList(makeContext(), operators, expressions);
        }
        finally {

            allocator.freeList(expressions);
        }

        return result;
    }
}
