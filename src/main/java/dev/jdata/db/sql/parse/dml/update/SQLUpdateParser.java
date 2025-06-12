package dev.jdata.db.sql.parse.dml.update;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValue;
import dev.jdata.db.sql.ast.statements.dml.SQLColumnValueUpdateValues;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateValues;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public class SQLUpdateParser extends SQLStatementParser {

    private final SQLExpressionParser expressionParser;
    private final SQLWhereClauseParser whereClauseParser;

    public SQLUpdateParser(SQLExpressionParser expressionParser, SQLWhereClauseParser whereClauseParser) {

        this.expressionParser = Objects.requireNonNull(expressionParser);
        this.whereClauseParser = Objects.requireNonNull(whereClauseParser);
    }

    public <E extends Exception, I extends CharInput<E>> SQLUpdateStatement parseUpdate(SQLExpressionLexer<E, I> lexer, long updateKeyword) throws ParserException, E {

        Objects.requireNonNull(lexer);
        checkIsKeyword(updateKeyword);

        final long tableName = lexer.lexName();

        final SQLUpdateValues updateValues = parseUpdateValues(lexer);

        final long setKeyword = lexer.lexKeyword(SQLToken.SET);

        final SQLWhereClause whereClause = whereClauseParser.parseWhereClause(lexer);

        return new SQLUpdateStatement(makeContext(), updateKeyword, tableName, setKeyword, updateValues, whereClause);
    }

    private <E extends Exception, I extends CharInput<E>> SQLUpdateValues parseUpdateValues(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLColumnValueUpdateValue> updateValues = allocator.allocateList(100);

        final SQLColumnValueUpdateValues result;

        try {
            for (;;) {

                final long columnName = lexer.lexName();

                lexer.lexExpect(SQLToken.EQ);

                final Expression expression = expressionParser.parseExpression(lexer);

                final SQLColumnValueUpdateValue updateValue = new SQLColumnValueUpdateValue(makeContext(), columnName, expression);

                updateValues.add(updateValue);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }

            result = new SQLColumnValueUpdateValues(makeContext(), updateValues);
        }
        finally {

            allocator.freeList(updateValues);
        }

        return result;
    }
}
