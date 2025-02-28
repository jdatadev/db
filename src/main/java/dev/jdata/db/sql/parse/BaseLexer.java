package dev.jdata.db.sql.parse;

import java.io.IOException;
import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.IToken;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

abstract class BaseLexer<T extends Enum<T> & IToken> {

    private final Lexer<T, CharInput> lexer;

    BaseLexer(Lexer<T, CharInput> lexer) {

        this.lexer = Objects.requireNonNull(lexer);
    }

    public final long getStringRef() {

        return lexer.getStringRef();
    }

    public final long getStringRef(int startPos, int endSkip) {

        return lexer.getStringRef(startPos, endSkip);
    }

    public final ParserException unexpectedToken(T expectedToken) {

        return lexer.unexpectedToken(expectedToken);
    }

    public final ParserException unexpectedToken(T[] expectedTokens) {

        return lexer.unexpectedToken(expectedTokens);
    }

    public final long getTokenLength() {

        return lexer.getTokenLength();
    }

    public final long lexKeyword(T toFind) throws ParserException, IOException {

        return LexerUtil.lexKeyword(lexer, toFind);
    }

    public final long lexStringRef(T toFind) throws ParserException, IOException {

        return LexerUtil.lexStringRef(lexer, toFind);
    }

    public final void lexSkip(T toFind) throws ParserException, IOException {

        LexerUtil.lexSkip(lexer, toFind);
    }

    public final void lexExpect(T toFind) throws ParserException, IOException {

        LexerUtil.lexExpect(lexer, toFind);
    }

    public final boolean lex(T toFind) throws IOException {

        return LexerUtil.lex(lexer, toFind);
    }

    public final T lex(T[] toFind) throws IOException {

        return LexerUtil.lex(lexer, toFind);
    }

    public final boolean peek(T toFind) throws IOException {

        return LexerUtil.peek(lexer, toFind);
    }

    public final T peek(T[] toFind) throws IOException {

        return LexerUtil.peek(lexer, toFind);
    }
}
