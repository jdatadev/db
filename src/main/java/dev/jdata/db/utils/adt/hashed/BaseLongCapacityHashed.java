package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongCapacityHashed<T> extends BaseHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    private static final Class<?> debugClass = BaseIntCapacityHashed.class;

    private long capacity;
    private long keyMask;

    protected abstract long increaseCapacity(long currentCapacity);

    protected abstract T rehash(T hashed, long newCapacity, long keyMask);

    BaseLongCapacityHashed(long initialCapacity, float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed) {
        super(loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacity", initialCapacity).add("loadFactor=", loadFactor).add("createHashed", createHashed));
        }

        this.capacity = Checks.isInitialCapacity(initialCapacity);
        this.keyMask = makeKeyMask(capacity);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    protected final long checkCapacity(int numAdditionalElements) {

        Checks.isLengthAboveZero(numAdditionalElements);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity));
        }

        final long newCapacity = checkCapacityAndRehash(numAdditionalElements, capacity, this, i -> i.increaseCapacity(i.capacity),
                (h, c, i) -> i.rehash(h, c, makeKeyMask(c)));

        final long result;

        if (newCapacity != -1L) {

            result = this.keyMask = makeKeyMask(newCapacity);

            this.capacity = Integers.checkUnsignedLongToUnsignedInt(newCapacity);
        }
        else {
            result = this.keyMask;
        }

        if (DEBUG) {

            PrintDebug.exitWithHex(debugClass, result);
        }

        return result;
    }

    protected final long getKeyMask() {
        return keyMask;
    }

    private static long makeKeyMask(long capacity) {

        final int capacityExponent = CapacityExponents.computeCapacityExponent(capacity) - 1;

        if (capacityExponent < 0) {

            throw new IllegalArgumentException();
        }

        return CapacityExponents.makeLongKeyMask(capacityExponent);
    }
}
