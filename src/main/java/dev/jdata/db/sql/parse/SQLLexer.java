package dev.jdata.db.sql.parse;

import java.util.function.Function;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

public class SQLLexer<E extends Exception, INPUT extends CharInput<E>> extends BaseLexer<SQLToken, E, INPUT> {

    SQLLexer() {

    }

    final void initialize(INPUT input, Function<String, E> createEOFException) {

        super.initialize(createLexer(input, createEOFException));
    }

    public final long lexName() throws ParserException, E {

        return lexStringRef(SQLToken.NAME);
    }

    private static <E extends Exception, I extends CharInput<E>> Lexer<SQLToken, E, I> createLexer(I input, Function<String, E> createEOFException) {

        return new Lexer<>(
                input,
                SQLToken.class,
                SQLToken.NONE,
                SQLToken.EOF,
                new SQLToken [] { SQLToken.WS },
                new SQLToken [] { SQLToken.SQL_COMMENT },
                createEOFException);
    }
}
