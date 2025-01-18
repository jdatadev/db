package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.MutableElements;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseHashed<T> extends BaseNumElements implements MutableElements, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_HASHED;

    private static final Class<?> debugClass = BaseHashed.class;

    private final float loadFactor;
    private final IntFunction<T> createHashed;
    private final Consumer<T> clearHashed;

    private T hashed;
    private int capacity;

    protected abstract int computeCapacity();
    protected abstract T rehash(T hashed, int newCapacity);

    abstract int increaseCapacity();

    BaseHashed(int initialCapacity, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacity", initialCapacity).add("loadFactor=", loadFactor).add("createHashed", createHashed));
        }

        Checks.isInitialCapacity(initialCapacity);
        Checks.isLoadFactor(loadFactor);
        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.loadFactor = loadFactor;
        this.createHashed = createHashed;
        this.clearHashed = clearHashed;

        this.hashed = createHashed.apply(initialCapacity);

        clearHashed.accept(hashed);

        this.capacity = computeCapacity();

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    @Override
    public final void clear() {

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

    protected static boolean shouldRehash(long numElements, int capacity, float loadFactor) {

        final float load = numElements / (float)capacity;

        return load > loadFactor;
    }

    protected final void checkCapacity(int numAdditionalElements) {

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity));
        }

        if (shouldRehash(getNumElements() + numAdditionalElements, capacity, loadFactor)) {

            final int newCapacity = increaseCapacity();

            if (DEBUG) {

                debug("rehash hashed to capacity " + capacity);
            }

            this.hashed = rehash(hashed, newCapacity);

            this.capacity = newCapacity;
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final T createHashed(int capacity) {

        Checks.isInitialCapacity(capacity);

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity));
        }

        final T result = createHashed.apply(capacity);

        clearHashed.accept(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
