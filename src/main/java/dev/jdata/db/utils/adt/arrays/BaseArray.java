package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseArray implements IArray, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY;

    private static boolean ASSERT = AssertionContants.ASSERT_BASE_ARRAY;

    private final boolean hasClearValue;

    abstract long getToStringLimit();

    BaseArray(boolean hasClearValue) {

        if (DEBUG) {

            enter(b -> b.add("hasClearValue", hasClearValue));
        }

        this.hasClearValue = hasClearValue;

        if (DEBUG) {

            exit();
        }
    }

    BaseArray(BaseArray toCopy) {

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        this.hasClearValue = toCopy.hasClearValue;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean hasClearValue() {
        return hasClearValue;
    }

    protected final boolean shouldClear() {

        return hasClearValue;
    }

    protected void assertShouldClear() {

        if (ASSERT) {

            Assertions.isTrue(hasClearValue);
        }
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(1000);

        sb.append(getClass().getSimpleName()).append(' ');

        toString(sb);

        return sb.toString();
    }
/*
    @FunctionalInterface
    interface ElementStringAppender<P> {

        void append(StringBuilder sb, long index, P parameter);
    }

    final <P> void toString(StringBuilder sb, long limit, P parameter, ElementStringAppender<P> appender) {

        Objects.requireNonNull(sb);
        Checks.isArrayLimit(limit);

        ByIndex.toString(this, 0L, limit, sb, null, null, null, null, (instance, index, b, a) -> instance.toString(index, b));
    }
*/

    @Override
    public final void toString(StringBuilder sb) {

        Objects.requireNonNull(sb);

        ByIndex.toString(this, 0L, getToStringLimit(), sb, null, (instance, index, b, a) -> instance.toString(index, b));
    }

    @Override
    public final void toHexString(StringBuilder sb) {

        Objects.requireNonNull(sb);

        ByIndex.toString(this, 0L, getToStringLimit(), sb, null, (instance, index, b, a) -> instance.toHexString(index, b));
    }
}
