package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseAnyDimensionalArray<T, U, V> extends BaseADTElements<T, U, V> implements IAnyDimensionalArray, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY;

    private static boolean ASSERT = AssertionContants.ASSERT_BASE_ARRAY;

    private final boolean hasClearValue;

    abstract long getToStringLimit();

    BaseAnyDimensionalArray(AllocationType allocationType, boolean hasClearValue) {
        super(allocationType);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("hasClearValue", hasClearValue));
        }

        this.hasClearValue = hasClearValue;

        if (DEBUG) {

            exit();
        }
    }

    BaseAnyDimensionalArray(AllocationType allocationType, BaseAnyDimensionalArray<T, U, V> toCopy) {
        super(allocationType);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
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

    protected final void assertShouldClear() {

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
