package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;

import org.jutils.io.strings.CharInput;

import dev.jdata.db.sql.ast.ISQLAllocator;

public class SQLAllocatorLexer<E extends Exception, INPUT extends CharInput<E>> extends SQLLexer<E, INPUT> {

    private ISQLAllocator allocator;

    final void initialize(INPUT input, Function<String, E> createEOFException, ISQLAllocator allocator) {

        if (this.allocator != null) {

            throw new IllegalStateException();
        }

        super.initialize(input, createEOFException);

        this.allocator = Objects.requireNonNull(allocator);
    }

    @Override
    public void reset() {

        if (allocator == null) {

            throw new IllegalStateException();
        }

        super.reset();

        this.allocator = null;
    }

    public final ISQLAllocator getAllocator() {
        return allocator;
    }
}
