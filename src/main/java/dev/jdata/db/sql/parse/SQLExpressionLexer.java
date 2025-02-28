package dev.jdata.db.sql.parse;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;

public final class SQLExpressionLexer extends SQLAllocatorLexer {

    private final StringResolver stringResolver;
    private final SQLScratchExpressionValues scratchExpressionValues;

    SQLExpressionLexer(CharInput charInput, SQLAllocator allocator, StringResolver stringResolver, SQLScratchExpressionValues scratchExpressionValues) {
        super(charInput, allocator);

        this.stringResolver = Objects.requireNonNull(stringResolver);
        this.scratchExpressionValues = Objects.requireNonNull(scratchExpressionValues);
    }

    public StringResolver getStringResolver() {
        return stringResolver;
    }
    public SQLScratchExpressionValues getScratchExpressionValues() {
        return scratchExpressionValues;
    }
}
