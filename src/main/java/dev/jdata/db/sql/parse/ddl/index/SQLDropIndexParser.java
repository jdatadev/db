package dev.jdata.db.sql.parse.ddl.index;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.index.SQLDropIndexStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;

public class SQLDropIndexParser extends SQLStatementParser {

    public <E extends Exception, I extends CharInput<E>> SQLDropIndexStatement parseDropIndex(SQLLexer<E, I> lexer, long dropKeyword, long indexKeyword)
            throws ParserException, E {

        Objects.requireNonNull(lexer);
        checkIsKeyword(dropKeyword);
        checkIsKeyword(indexKeyword);

        final long name = lexer.lexName();

        return new SQLDropIndexStatement(makeContext(), dropKeyword, indexKeyword, name);
    }
}
