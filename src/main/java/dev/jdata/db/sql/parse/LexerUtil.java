package dev.jdata.db.sql.parse;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.IToken;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

class LexerUtil {

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> long lexKeyword(Lexer<T, E, I> lexer, T toFind) throws ParserException, E {

        return lexStringRef(lexer, toFind);
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> long lexStringRef(Lexer<T, E, I> lexer, T toFind) throws ParserException, E {

        lexExpect(lexer, toFind);

        return lexer.getStringRef();
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> void lexSkip(Lexer<T, E, I> lexer, T toFind) throws ParserException, E {

        lexExpect(lexer, toFind);
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> void lexExpect(Lexer<T, E, I> lexer, T toFind) throws ParserException, E {

        if (!lex(lexer, toFind)) {

            throw lexer.unexpectedToken(toFind);
        }
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> boolean lex(Lexer<T, E, I> lexer, T toFind) throws E {

        return lexer.lexSkipWSAndComment(toFind) == toFind;
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> T lex(Lexer<T, E, I> lexer, T[] toFind) throws E {

        return lexer.lexSkipWSAndComment(toFind);
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> boolean peek(Lexer<T, E, I> lexer, T toFind) throws E {

        lexer.skipAnyWS();

        return lexer.peekOne(toFind).equals(toFind);
    }

    static <T extends Enum<T> & IToken, E extends Exception, I extends CharInput<E>> T peek(Lexer<T, E, I> lexer, T[] toFind) throws E {

        lexer.skipAnyWS();

        return lexer.peek(toFind);
    }
}
