package dev.jdata.db.sql.parse.ddl.index;

import java.io.IOException;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.index.SQLDropIndexStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;

public class SQLDropIndexParser extends SQLStatementParser {

    public SQLDropIndexStatement parseDropIndex(SQLLexer lexer, long dropKeyword, long indexKeyword) throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        checkIsKeyword(dropKeyword);
        checkIsKeyword(indexKeyword);

        final long name = lexer.lexName();

        return new SQLDropIndexStatement(makeContext(), dropKeyword, indexKeyword, name);
    }
}
