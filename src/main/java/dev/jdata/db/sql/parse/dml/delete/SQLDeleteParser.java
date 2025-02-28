package dev.jdata.db.sql.parse.dml.delete;

import java.io.IOException;
import java.util.Objects;

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

    public SQLDeleteStatement parseDelete(SQLExpressionLexer lexer, long deleteKeyword) throws ParserException, IOException {

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
