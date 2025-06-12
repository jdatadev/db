package dev.jdata.db.sql.parse.dml.insert;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;

public final class SQLInsertParser extends SQLStatementParser {

    private final SQLExpressionParser expressionParser;

    public SQLInsertParser(SQLExpressionParser expressionParser) {

        this.expressionParser = Objects.requireNonNull(expressionParser);
    }

    public <E extends Exception, I extends CharInput<E>> SQLInsertStatement parseInsert(SQLExpressionLexer<E, I> lexer, long insertKeyword) throws ParserException, E {

        Objects.requireNonNull(lexer);
        checkIsKeyword(insertKeyword);

        final long intoKeyword = lexer.lexKeyword(SQLToken.INTO);

        final long tableName = lexer.lexName();

        final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

        final long valuesKeyword = lexer.lexKeyword(SQLToken.VALUES);

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<Expression> expressions = allocator.allocateList(100);

        lexer.lexExpect(SQLToken.LPAREN);

        try {
            for (;;) {

                final Expression expression = expressionParser.parseExpression(lexer);

                expressions.add(expression);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }
        }
        finally {

            allocator.freeList(expressions);
        }

        lexer.lexExpect(SQLToken.RPAREN);

        return new SQLInsertStatement(makeContext(), insertKeyword, intoKeyword, tableName, columnNames, valuesKeyword, expressions);
    }
}
