package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;

public final class SQLExpressionLexer<E extends Exception, INPUT extends CharInput<E>> extends SQLAllocatorLexer<E, INPUT> {

    private StringResolver stringResolver;
    private SQLScratchExpressionValues scratchExpressionValues;

    public SQLExpressionLexer(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(INPUT input, Function<String, E> createEOFException, ISQLAllocator allocator, StringResolver stringResolver,
            SQLScratchExpressionValues scratchExpressionValues) {

        if (this.stringResolver != null) {

            throw new IllegalStateException();
        }

        super.initialize(input, createEOFException, allocator);

        this.stringResolver = Objects.requireNonNull(stringResolver);
        this.scratchExpressionValues = Objects.requireNonNull(scratchExpressionValues);
    }

    @Override
    public void reset() {

        if (stringResolver == null) {

            throw new IllegalStateException();
        }

        super.reset();

        this.stringResolver = null;
        this.scratchExpressionValues = null;
    }

    public StringResolver getStringResolver() {
        return stringResolver;
    }
    public SQLScratchExpressionValues getScratchExpressionValues() {
        return scratchExpressionValues;
    }
}
