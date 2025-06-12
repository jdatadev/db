package dev.jdata.db.sql.parse.dml.delete;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public class SQLDeleteParser extends SQLStatementParser {

    private final SQLWhereClauseParser whereClauseParser;

    public SQLDeleteParser(SQLWhereClauseParser whereClauseParser) {

        this.whereClauseParser = Objects.requireNonNull(whereClauseParser);
    }

    public <E extends Exception, I extends CharInput<E>> SQLDeleteStatement parseDelete(SQLExpressionLexer<E, I> lexer, long deleteKeyword) throws ParserException, E {

        Objects.requireNonNull(lexer);
        checkIsKeyword(deleteKeyword);

        final long fromKeyword = lexer.lexKeyword(SQLToken.FROM);

        final SQLObjectName objectName = parseObjectName(lexer);

        final long asKeyword = lexer.lexKeyword(SQLToken.AS);

        final long alias = lexer.lexName();

        final SQLWhereClause whereClause = whereClauseParser.parseWhereClause(lexer);

        return new SQLDeleteStatement(makeContext(), deleteKeyword, fromKeyword, objectName, asKeyword, alias, whereClause);
    }
}
