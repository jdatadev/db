package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntCapacityHashed<T> extends BaseHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    private static final Class<?> debugClass = BaseIntCapacityHashed.class;

    private int capacity;

    protected abstract int computeCapacity();
    protected abstract T rehash(T hashed, int newCapacity);

    abstract int increaseCapacity();

    BaseIntCapacityHashed(int initialCapacity, float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed) {
        super(loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed));
        }

        this.capacity = Checks.isInitialCapacity(initialCapacity);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    protected final void checkCapacity(int numAdditionalElements) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity));
        }

        final long newCapacity = checkCapacityAndRehash(numAdditionalElements, capacity, this, i -> i.increaseCapacity(),
                (h, c, i) -> i.rehash(h, Integers.checkUnsignedLongToUnsignedInt(c)));

        if (newCapacity != -1L) {

            this.capacity = Integers.checkUnsignedLongToUnsignedInt(newCapacity);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }
}
