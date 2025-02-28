package dev.jdata.db.sql.parse;

import java.util.Objects;

import org.jutils.io.strings.CharInput;

import dev.jdata.db.sql.ast.SQLAllocator;

public class SQLAllocatorLexer extends SQLLexer {

    private final SQLAllocator allocator;

    SQLAllocatorLexer(CharInput charInput, SQLAllocator allocator) {
        super(charInput);

        this.allocator = Objects.requireNonNull(allocator);
    }

    public final SQLAllocator getAllocator() {
        return allocator;
    }
}
