package dev.jdata.db.sql.parse;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.IToken;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class BaseLexer<TOKEN extends Enum<TOKEN> & IToken, E extends Exception, INPUT extends CharInput<E>> extends ObjectCacheNode implements IResettable {

    private Lexer<TOKEN, E, INPUT> lexer;

    BaseLexer(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(Lexer<TOKEN, E, INPUT> lexer) {

        if (this.lexer != null) {

            throw new IllegalStateException();
        }

        this.lexer = Objects.requireNonNull(lexer);
    }

    @Override
    public void reset() {

        if (lexer == null) {

            throw new IllegalStateException();
        }

        this.lexer = null;
    }

    public final long getStringRef() {

        return lexer.getStringRef();
    }

    public final long getStringRef(int startPos, int endSkip) {

        return lexer.getStringRef(startPos, endSkip);
    }

    public final ParserException unexpectedToken(TOKEN expectedToken) {

        return lexer.unexpectedToken(expectedToken);
    }

    public final ParserException unexpectedToken(TOKEN[] expectedTokens) {

        return lexer.unexpectedToken(expectedTokens);
    }

    public final long getTokenLength() {

        return lexer.getTokenLength();
    }

    public final long lexKeyword(TOKEN toFind) throws ParserException, E {

        return LexerUtil.lexKeyword(lexer, toFind);
    }

    public final long lexStringRef(TOKEN toFind) throws ParserException, E {

        return LexerUtil.lexStringRef(lexer, toFind);
    }

    public final void lexSkip(TOKEN toFind) throws ParserException, E {

        LexerUtil.lexSkip(lexer, toFind);
    }

    public final void lexExpect(TOKEN toFind) throws ParserException, E {

        LexerUtil.lexExpect(lexer, toFind);
    }

    public final boolean lex(TOKEN toFind) throws E {

        return LexerUtil.lex(lexer, toFind);
    }

    public final TOKEN lex(TOKEN[] toFind) throws E {

        return LexerUtil.lex(lexer, toFind);
    }

    public final boolean peek(TOKEN toFind) throws E {

        return LexerUtil.peek(lexer, toFind);
    }

    public final TOKEN peek(TOKEN[] toFind) throws E {

        return LexerUtil.peek(lexer, toFind);
    }
}
