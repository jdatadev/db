package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseCapacityHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES>

        extends BaseHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> {

    interface WithCapacityExponentInstantiator3<T, P1, P2, P3> {

        T instantiate(AllocationType allocationType, int capacityExponent, P1 parameter1, P2 parameter2, P3 parameter3);
    }

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_CAPACITY_HASHED;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_CAPACITY_HASHED;

    protected static final float DEFAULT_LOAD_FACTOR = HashedConstants.DEFAULT_LOAD_FACTOR;
    protected static final int DEFAULT_INITIAL_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INITIAL_CAPACITY_EXPONENT;
    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = HashedConstants.DEFAULT_CAPACITY_EXPONENT_INCREASE;
    protected static final int DEFAULT_INNER_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INNER_CAPACITY_EXPONENT;

    private static final int DEFAULT_NON_BUCKET_OUTER_INITIAL_CAPACITY_EXPONENT = 0;
    protected static final int DEFAULT_NON_BUCKET_OUTER_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DEFAULT_NON_BUCKET_OUTER_INITIAL_CAPACITY_EXPONENT);

    private static final int DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY_EXPONENT = 0;
    protected static final int DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY_EXPONENT);
    protected static final int DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT = 14;

    private static final int DEFAULT_CAPACITY_INCREASE_SHIFT = 2;

    protected static int computeRehashCapacityExponent(long numElements, float loadFactor) {

        return HashUtil.computeRehashCapacityExponent(numElements, loadFactor);
    }

    private final float loadFactor;
    private final boolean isLargeHashed;
    private final LongFunction<HASHED> createHashed;

    private long capacity;

    abstract long adjustNewCapacity(long requiredCapacity);

    BaseCapacityHashed(AllocationType allocationType, long initialCapacity, float loadFactor, CapacityMax capacityMax, LongFunction<HASHED> createHashed,
            Consumer<HASHED> clearHashed, Supplier<HASHED> recreateHashed) {
        super(allocationType, () -> createHashed.apply(initialCapacity), clearHashed, recreateHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("capacityMax", capacityMax)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        Checks.isIntInitialCapacityAboveZero(initialCapacity);
        Checks.isLoadFactor(loadFactor);
        Objects.requireNonNull(capacityMax);
        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.loadFactor = loadFactor;
        this.isLargeHashed = capacityMax == CapacityMax.LONG;
        this.createHashed = createHashed;

        this.capacity = initialCapacity;

        if (DEBUG) {

            exit();
        }
    }

    BaseCapacityHashed(AllocationType allocationType, BaseCapacityHashed<HASHED, ?, ?, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        this.loadFactor = toInitializeFrom.loadFactor;
        this.isLargeHashed = toInitializeFrom.isLargeHashed;
        this.createHashed = null;
    }

    BaseCapacityHashed(AllocationType allocationType, BaseCapacityHashed<HASHED, ?, ?, ?> toCopy, Function<HASHED, HASHED> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        this.loadFactor = toCopy.loadFactor;
        this.isLargeHashed = toCopy.isLargeHashed;
        this.createHashed = toCopy.createHashed;

        if (DEBUG) {

            exit();
        }
    }

    final int getIntCapacity() {

        return Capacity.intCapacityRenamed(capacity);
    }

    final long getLongCapacity() {
        return capacity;
    }

    long computeNewCapacityForIncrease(long currentCapacity) {

        return currentCapacity << DEFAULT_CAPACITY_INCREASE_SHIFT;
    }

    final <P> int increaseCapacityAndRehashReturnIntCapacity(P parameter, Rehasher<HASHED, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = increaseCapacityAndRehashAndReturnLongCapacity(parameter, rehasher);

        final int result = Integers.checkUnsignedLongToUnsignedInt(newCapacity);

        if (DEBUG) {

            exit(result, b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        return result;
    }

    final <P> long increaseCapacityAndRehashAndReturnLongCapacity(P parameter, Rehasher<HASHED, P> rehasher) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = increaseCapacityAndRehash(getLongCapacity(), parameter, rehasher);

        this.capacity = newCapacity;

        if (DEBUG) {

            exit(newCapacity, b -> b.add("parameter", parameter).add("rehasher", rehasher));
        }

        return newCapacity;
    }

    private <P> long increaseCapacityAndRehash(long capacity, P parameter, Rehasher<HASHED, P> rehasher) {

        Checks.isIntOrLongCapacityAboveZero(capacity);
        Objects.requireNonNull(rehasher);

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        final long newCapacity = computeNewCapacityForIncrease(capacity);

        if (ASSERT) {

            Assertions.areEqual(newCapacity, adjustNewCapacity(newCapacity));
        }

        final HASHED newHashed = createHashed.apply(newCapacity);

        rehash(newHashed, newCapacity, parameter, rehasher);

        if (DEBUG) {

            exit(newCapacity, b -> b.add("capacity", capacity).add("parameter", parameter).add("rehasher", rehasher));
        }

        return newCapacity;
    }

    final <P> void checkCapacity(long numAdditionalElements, P parameter, Rehasher<HASHED, P> rehasher) {

        Checks.isLongNumElementsAboveZero(numAdditionalElements);
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

    private <P> long checkCapacityAndRehash(long numAdditionalElements, long capacity, P parameter, Rehasher<HASHED, P> rehasher) {

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

            final HASHED newHashed = createHashed.apply(newCapacity);

            rehash(newHashed, newCapacity, parameter, rehasher);
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
