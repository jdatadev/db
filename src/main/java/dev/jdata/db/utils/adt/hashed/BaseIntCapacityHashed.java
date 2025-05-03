package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

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

    BaseIntCapacityHashed(BaseIntCapacityHashed<T> toCopy, T copyOfHashed) {
        super(toCopy, copyOfHashed);
    }

    protected final int getCapacity() {
        return capacity;
    }

    protected final <P> int increaseCapacityAndRehash(P parameter, ToLongFunction<P> capacityIncreaser, Rehasher<T, P> rehasher) {

        if (DEBUG) {

            PrintDebug.enter(debugClass);
        }

        final long newCapacity = increaseCapacityAndRehash(capacity, parameter, capacityIncreaser, rehasher);

        final int result = this.capacity = Integers.checkUnsignedLongToUnsignedInt(newCapacity);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    protected final int increaseCapacityAndRehash() {

        if (DEBUG) {

            PrintDebug.enter(debugClass);
        }

        final int result = increaseCapacityAndRehash(this, i -> i.increaseCapacity(), (h, c, i) -> i.rehash(h, Integers.checkUnsignedLongToUnsignedInt(c)));

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
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
