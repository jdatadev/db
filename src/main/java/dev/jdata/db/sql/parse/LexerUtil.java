package dev.jdata.db.sql.parse;

import java.io.IOException;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.IToken;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

class LexerUtil {

    static <T extends Enum<T> & IToken> long lexKeyword(Lexer<T, CharInput> lexer, T toFind) throws ParserException, IOException {

        return lexStringRef(lexer, toFind);
    }

    static <T extends Enum<T> & IToken> long lexStringRef(Lexer<T, CharInput> lexer, T toFind) throws ParserException, IOException {

        lexExpect(lexer, toFind);

        return lexer.getStringRef();
    }

    static <T extends Enum<T> & IToken> void lexSkip(Lexer<T, CharInput> lexer, T toFind) throws ParserException, IOException {

        lexExpect(lexer, toFind);
    }

    static <T extends Enum<T> & IToken> void lexExpect(Lexer<T, CharInput> lexer, T toFind) throws ParserException, IOException {

        if (!lex(lexer, toFind)) {

            throw lexer.unexpectedToken(toFind);
        }
    }

    static <T extends Enum<T> & IToken> boolean lex(Lexer<T, CharInput> lexer, T toFind) throws IOException {

        return lexer.lexSkipWSAndComment(toFind) == toFind;
    }

    static <T extends Enum<T> & IToken> T lex(Lexer<T, CharInput> lexer, T[] toFind) throws IOException {

        return lexer.lexSkipWSAndComment(toFind);
    }

    static <T extends Enum<T> & IToken> boolean peek(Lexer<T, CharInput> lexer, T toFind) throws IOException {

        lexer.skipAnyWS();

        return lexer.peekOne(toFind).equals(toFind);
    }

    static <T extends Enum<T> & IToken> T peek(Lexer<T, CharInput> lexer, T[] toFind) throws IOException {

        lexer.skipAnyWS();

        return lexer.peek(toFind);
    }
}
