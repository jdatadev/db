package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseCapacityHashed<T> extends BaseHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_CAPACITY_HASHED;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_CAPACITY_HASHED;

    protected static final float DEFAULT_LOAD_FACTOR = HashedConstants.DEFAULT_LOAD_FACTOR;

    private static final int DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY_EXPONENT = 0;
    protected static final int DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY_EXPONENT);
    protected static final int DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT = 14;

    private static final int DEFAULT_CAPACITY_INCREASE_SHIFT = 2;

    protected static int computeRehashCapacityExponent(long numElements, float loadFactor) {

        return HashUtil.computeRehashCapacityExponent(numElements, loadFactor);
    }

    private final float loadFactor;
    private final boolean isLargeHashed;

    private long capacity;

    abstract long adjustNewCapacity(long requiredCapacity);

    BaseCapacityHashed(long initialCapacity, float loadFactor, boolean isLargeHashed, Supplier<T> createHashed, Consumer<T> clearHashed) {
        super(createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("isLargeHashed", isLargeHashed).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed));
        }

        Checks.isInitialCapacity(initialCapacity);
        Checks.isLoadFactor(loadFactor);
        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.loadFactor = loadFactor;
        this.isLargeHashed = isLargeHashed;

        this.capacity = initialCapacity;

        if (DEBUG) {

            exit();
        }
    }

    BaseCapacityHashed(BaseCapacityHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);

        this.loadFactor = toCopy.loadFactor;
        this.isLargeHashed = toCopy.isLargeHashed;
    }

    final int getIntCapacity() {

        return Integers.checkUnsignedLongToUnsignedInt(capacity);
    }

    final long getLongCapacity() {
        return capacity;
    }

    long computeNewCapacityForIncrease(long currentCapacity) {

        return currentCapacity << DEFAULT_CAPACITY_INCREASE_SHIFT;
    }

    final <P> int increaseCapacityAndRehashReturnIntCapacity(P parameter, Rehasher<T, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = increaseCapacityAndRehash(parameter, rehasher);

        final int result = Integers.checkUnsignedLongToUnsignedInt(newCapacity);

        if (DEBUG) {

            exit(result, b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        return result;
    }

    private <P> long increaseCapacityAndRehash(P parameter, Rehasher<T, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = increaseCapacityAndRehash(getLongCapacity(), parameter, rehasher);

        final long result = this.capacity = Integers.checkUnsignedLongToUnsignedInt(newCapacity);

        if (DEBUG) {

            exit(result, b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        return result;
    }

    private <P> long increaseCapacityAndRehash(long capacity, P parameter, Rehasher<T, P> rehasher) {

        Checks.isCapacity(capacity);
        Objects.requireNonNull(rehasher);

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = computeNewCapacityForIncrease(capacity);

        if (ASSERT) {

            Assertions.areEqual(newCapacity, adjustNewCapacity(newCapacity));
        }

        rehash(newCapacity, parameter, rehasher);

        if (DEBUG) {

            exit(newCapacity, b -> b.add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        return newCapacity;
    }

    final <P> void checkCapacity(long numAdditionalElements, P parameter, Rehasher<T, P> rehasher) {

        Checks.isNumElementsAboveZero(numAdditionalElements);
        Objects.requireNonNull(rehasher);

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements).add("parameter", parameter).add("rehasher", rehasher).add("capacity", capacity));
        }

        final long newCapacity = checkCapacityAndRehash(numAdditionalElements, capacity, parameter, rehasher);

        if (newCapacity != -1L) {

            this.capacity = newCapacity;
        }

        if (DEBUG) {

            exit(b -> b.add("numAdditionalElements", numAdditionalElements).add("parameter", parameter).add("rehasher", rehasher).add("capacity", capacity));
        }
    }

    private <P> long checkCapacityAndRehash(long numAdditionalElements, long capacity, P parameter, Rehasher<T, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity;

        final long requiredNumElements = getNumElements() + numAdditionalElements;

        if (HashUtil.shouldRehash(requiredNumElements, capacity, loadFactor)) {

            final long requiredCapacity = HashUtil.computeRequiredCapacity(requiredNumElements, capacity, loadFactor, DEFAULT_CAPACITY_INCREASE_SHIFT, isLargeHashed);

            newCapacity = adjustNewCapacity(requiredCapacity);

            if (DEBUG) {

                debug("rehash hashed to capacity " + newCapacity + " from " + capacity);
            }

            rehash(newCapacity, parameter, rehasher);
        }
        else {
            newCapacity = -1L;
        }

        if (DEBUG) {

            exit(newCapacity, b -> b.add("numAdditionalElements", numAdditionalElements).add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        return newCapacity;
    }
}
