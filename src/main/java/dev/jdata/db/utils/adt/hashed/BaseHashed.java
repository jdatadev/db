package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseHashed<T> extends BaseNumElements implements IMutableElements, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_HASHED;

    private static final Class<?> debugClass = BaseHashed.class;

    protected static final int BUCKETS_OUTER_INITIAL_CAPACITY = 1;
    protected static final int BUCKETS_INNER_CAPACITY_EXPONENT = 14;

    private final float loadFactor;
    private final Consumer<T> clearHashed;

    private T hashed;

    BaseHashed(float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("loadFactor", loadFactor).add("createHashed", createHashed));
        }

        Checks.isLoadFactor(loadFactor);
        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.loadFactor = loadFactor;
        this.clearHashed = clearHashed;

        this.hashed = createHashed.get();

        clearHashed.accept(hashed);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        super.clearNumElements();

        clearHashed.accept(hashed);

        if (DEBUG) {

            exit();
        }
    }

    protected final T getHashed() {
        return hashed;
    }

    protected static boolean shouldRehash(long numElements, long capacity, float loadFactor) {

        if (capacity > Double.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        final double load = numElements / (double)capacity;

        return load > loadFactor;
    }

    @FunctionalInterface
    interface Rehasher<T, P> {

        T rehash(T hashed, long newCapacity, P parameter);
    }

    final <P> long checkCapacityAndRehash(long numAdditionalElements, long capacity, P parameter, ToLongFunction<P> capacityIncreaser, Rehasher<T, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity));
        }

        final long newCapacity;

        if (shouldRehash(getNumElements() + numAdditionalElements, capacity, loadFactor)) {

            newCapacity = capacityIncreaser.applyAsLong(parameter);

            if (DEBUG) {

                debug("rehash hashed to capacity " + capacity);
            }

            this.hashed = rehasher.rehash(hashed, newCapacity, parameter);
        }
        else {
            newCapacity = -1L;
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, newCapacity);
        }

        return newCapacity;
    }

    final void clearHashed(T hashed) {

        Objects.requireNonNull(hashed);

        clearHashed.accept(hashed);
    }
}
