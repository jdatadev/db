package dev.jdata.db.schema.storage.sqloutputter;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.scalars.Integers;

abstract class ExceptionAppendableSQLOutputter<P, E extends Exception> extends BaseSQLOutputter<E> implements IResettable {

    private ICharactersBufferAllocator charactersBufferAllocator;
    private P parameter;
    private ExceptionAppendable<P, E> appendable;

    final void initialize(ICharactersBufferAllocator charactersBufferAllocator, P parameter, ExceptionAppendable<P, E> appendable) {

        this.charactersBufferAllocator = Objects.requireNonNull(charactersBufferAllocator);
        this.parameter = parameter;
        this.appendable = Objects.requireNonNull(appendable);

        super.initialize();
    }

    @Override
    public void reset() {

        super.reset();

        this.charactersBufferAllocator = null;
        this.parameter = null;
        this.appendable = null;
    }

    @Override
    final void append(char c) throws E {

        appendable.append(c, parameter);
    }

    @Override
    final void append(int integer) throws E {

        Integers.toChars(integer, this, (c, i) -> i.appendable.append(c, i.parameter));
    }

    @Override
    final void append(String string) throws E {

        appendable.append(string, parameter);
    }

    @Override
    final void appendString(long stringRef, StringResolver stringResolver) throws E {

        stringResolver.makeString(stringRef, this, charactersBufferAllocator, (b, n, i) -> {

            final ExceptionAppendable<P, E> appendable = i.appendable;
            final P parameter = i.parameter;

            for (int characterIndex = 0; characterIndex < n; ++ characterIndex) {

                appendable.append(b[characterIndex], parameter);
            }

            return null;
        });
    }

    final P getParameter() {
        return parameter;
    }
}
