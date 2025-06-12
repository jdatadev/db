package dev.jdata.db.schema.storage.sqloutputter;

import java.util.Objects;

import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

public final class StringBuilderSQLOutputter extends TextSQLOutputter<StringBuilder, RuntimeException> {

    private StringBuilder sb;

    public void initialize(ICharactersBufferAllocator charactersBufferAllocator, StringBuilder sb) {

        if (this.sb != null) {

            throw new IllegalStateException();
        }

        this.sb = Objects.requireNonNull(sb);

        super.initialize(charactersBufferAllocator, sb, (c, b) -> b.append(c));
    }

    @Override
    public void reset() {

        if (this.sb == null) {

            throw new IllegalStateException();
        }

        super.reset();
    }
}
