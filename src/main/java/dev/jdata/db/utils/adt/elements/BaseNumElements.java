package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.debug.PrintDebug;

public abstract class BaseNumElements extends BaseElements implements PrintDebug {

    private static final boolean DEBUG = Boolean.FALSE;

    private long numElements;

    public final boolean isEmpty() {

        return numElements == 0;
    }

    public final long getNumElements() {
        return numElements;
    }

    protected final void clearNumElements() {

        if (DEBUG) {

            enter();
        }

        this.numElements = 0;

        if (DEBUG) {

            exit(numElements);
        }
    }

    protected final void incrementNumElements() {

        if (DEBUG) {

            enter();
        }

        ++ numElements;

        if (DEBUG) {

            exit(numElements);
        }
    }

    protected final void decrementNumElements() {

        if (DEBUG) {

            enter();
        }

        if (numElements == 0L) {

            throw new IllegalStateException();
        }

        -- numElements;

        if (DEBUG) {

            exit(numElements);
        }
    }
}
