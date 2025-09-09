package dev.jdata.db.schema.storage.sqloutputter;

import java.util.Objects;

import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.IResettable;

public abstract class TextSQLOutputter<P, E extends Exception> extends ExceptionAppendableSQLOutputter<TextSQLOutputter<P, E>, E> implements IResettable {

    @FunctionalInterface
    public interface CharOutputter<P, E extends Exception> {

        void output(char c, P parameter) throws E;
    }

    private P parameter;
    private CharOutputter<P, E> charOutputter;

    protected final void initialize(ICharactersBufferAllocator charactersBufferAllocator, P parameter, CharOutputter<P, E> charOutputter) {

        if (this.charOutputter != null) {

            throw new IllegalStateException();
        }

        this.parameter = parameter;
        this.charOutputter = Objects.requireNonNull(charOutputter);

        final ExceptionAppendable<TextSQLOutputter<P, E>, E> appendable = (c, i) -> {

            i.charOutputter.output(c, i.parameter);
        };

        initialize(charactersBufferAllocator, this, appendable);
    }

    @Override
    public void reset() {

        super.reset();

        this.parameter = null;
        this.charOutputter = null;
    }
}
