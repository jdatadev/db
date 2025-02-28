package dev.jdata.db.sql.parse;

import java.io.IOException;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

public class SQLLexer extends BaseLexer<SQLToken> {

    SQLLexer(CharInput data) {
        super(createLexer(data));
    }

    public final long lexName() throws ParserException, IOException {

        return lexStringRef(SQLToken.NAME);
    }

    private static Lexer<SQLToken, CharInput> createLexer(CharInput data) {

        final Lexer<SQLToken, CharInput> lexer = new Lexer<SQLToken, CharInput>(
                data,
                SQLToken.class,
                SQLToken.NONE,
                SQLToken.EOF,
                new SQLToken [] { SQLToken.WS },
                new SQLToken [] { SQLToken.SQL_COMMENT });

        return lexer;
    }
}
