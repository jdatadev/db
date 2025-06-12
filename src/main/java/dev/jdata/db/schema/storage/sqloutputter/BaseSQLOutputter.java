package dev.jdata.db.schema.storage.sqloutputter;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.sql.parse.SQLToken;

abstract class BaseSQLOutputter<E extends Exception> implements ISQLOutputter<E> {

    abstract void append(char c) throws E;
    abstract void append(int i) throws E;
    abstract void append(String string) throws E;
    abstract void appendString(long stringRef, StringResolver stringResolver) throws E;

    private boolean initialized;

    final void initialize() {

        if (initialized) {

            throw new IllegalStateException();
        }

        this.initialized = false;
    }

    @Override
    public void reset() {

        if (!initialized) {

            throw new IllegalStateException();
        }

        this.initialized = false;
    }

    @Override
    public final ISQLOutputter<E> appendSeparator() throws E {

        append(' ');

        return this;
    }

    @Override
    public final ISQLOutputter<E> appendKeyword(SQLToken sqlToken) throws E {

        Objects.requireNonNull(sqlToken);

        switch (sqlToken.getTokenType()) {

        case CHARACTER:

            append(sqlToken.getCharacter());
            break;

        case CI_LITERAL:
        case CS_LITERAL:

            append(sqlToken.getLiteral());
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return this;
    }

    @Override
    public final ISQLOutputter<E> appendName(long stringRef, StringResolver stringResolver) throws E {

        appendString(stringRef, stringResolver);

        return this;
    }

    @Override
    public final ISQLOutputter<E> appendStringLiteral(long stringRef, StringResolver stringResolver) throws E {

        final char singleQuote = '\'';

        append(singleQuote);
        appendString(stringRef, stringResolver);
        append(singleQuote);

        return this;
    }

    @Override
    public final ISQLOutputter<E> appendIntegerLiteral(int integer) throws E {

        append(integer);

        return this;
    }
}
