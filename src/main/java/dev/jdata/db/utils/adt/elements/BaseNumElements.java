package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.debug.PrintDebug;

public abstract class BaseNumElements extends BaseElements implements Elements, PrintDebug {

    private static final boolean DEBUG = Boolean.FALSE;

    private long numElements;

    @Override
    public final boolean isEmpty() {

        return numElements == 0;
    }

    @Override
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

    protected final void increaseNumElements() {

        if (DEBUG) {

            enter();
        }

        ++ numElements;

        if (DEBUG) {

            exit(numElements);
        }
    }

    protected final void decreaseNumElements() {

        if (DEBUG) {

            enter();
        }

        -- numElements;

        if (DEBUG) {

            exit(numElements);
        }
    }
}
