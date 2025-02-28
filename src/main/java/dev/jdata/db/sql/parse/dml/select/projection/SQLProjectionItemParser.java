package dev.jdata.db.sql.parse.dml.select.projection;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.expressions.SQLAggregateFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLColumnExpression;
import dev.jdata.db.sql.ast.expressions.SQLFunctionCallExpression;
import dev.jdata.db.sql.ast.statements.dml.SQLAggregateProjectionElement;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnProjectionElement;
import dev.jdata.db.sql.ast.statements.dml.SQLExpressionProjectionElement;
import dev.jdata.db.sql.ast.statements.dml.SQLFunctionProjectionElement;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionElement;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;

public class SQLProjectionItemParser extends BaseSQLParser {

    private final SQLExpressionParser expressionParser;

    public SQLProjectionItemParser(SQLExpressionParser expressionParser) {

        this.expressionParser = Objects.requireNonNull(expressionParser);
    }

    public final SQLProjectionItem parseProjectionItem(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLProjectionElement projectionElement = parseProjectionElement(lexer);

        final SQLProjectionItem result;

        if (lexer.peek(SQLToken.NAME)) {

            final long alias = lexer.lexName();

            result = new SQLProjectionItem(makeContext(), projectionElement, alias);
        }
        else {
            result = new SQLProjectionItem(makeContext(), projectionElement);
        }

        return result;
    }

    private SQLProjectionElement parseProjectionElement(SQLExpressionLexer lexer) throws ParserException, IOException {

        final Expression expression = expressionParser.parseExpression(lexer);

        final SQLProjectionElement result;

        if (expression instanceof SQLAggregateFunctionCallExpression) {

            result = new SQLAggregateProjectionElement(makeContext(), (SQLAggregateFunctionCallExpression)expression);
        }
        else if (expression instanceof SQLFunctionCallExpression) {

            result = new SQLFunctionProjectionElement(makeContext(), (SQLFunctionCallExpression)expression);
        }
        else if (expression instanceof SQLColumnExpression) {

            result = new SQLColumnProjectionElement(makeContext(), (SQLColumnExpression)expression);
        }
        else {
            result = new SQLExpressionProjectionElement(makeContext(), expression);
        }

        return result;
    }
}
