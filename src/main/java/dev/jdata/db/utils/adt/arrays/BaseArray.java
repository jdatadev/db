package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseArray implements IArray, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY;

    private static boolean ASSERT = AssertionContants.ASSERT_BASE_ARRAY;

    private final boolean hasClearValue;

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
}
