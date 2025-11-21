package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

public abstract class BaseLongCapacityExponentArrayHashed<T extends IMutableLargeArrayMarker> extends BaseLongCapacityArrayHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_CAPACITY_EXPONENT_ARRAY_HASHED;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_CAPACITY_EXPONENT_ARRAY_HASHED;

    protected static final int DEFAULT_INITIAL_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INITIAL_CAPACITY_EXPONENT;
    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = HashedConstants.DEFAULT_CAPACITY_EXPONENT_INCREASE;

    protected static int computeOuterCapacity(long newCapacity, int newCapacityExponent, int innerCapacityExponent) {

        Checks.isLongCapacityAboveZero(newCapacity);
        Checks.isLongCapacityExponent(newCapacityExponent);
        Checks.isIntCapacityExponent(innerCapacityExponent);
        Checks.areEqual(newCapacity, 1L << newCapacityExponent);
        Checks.isGreaterThanOrEqualTo(newCapacityExponent, innerCapacityExponent);

        return 1 << computeOuterCapacityExponent(newCapacityExponent, innerCapacityExponent);
    }

    protected static int computeOuterCapacityExponent(int newCapacityExponent, int innerCapacityExponent) {

        Checks.isLongCapacityExponent(newCapacityExponent);
        Checks.isIntCapacityExponent(innerCapacityExponent);
        Checks.isGreaterThanOrEqualTo(newCapacityExponent, innerCapacityExponent);

        return newCapacityExponent - innerCapacityExponent;
    }

    private static int getRecreateOuterCapacity() {

        return CapacityExponents.computeIntCapacityFromExponent(DEFAULT_NON_BUCKET_OUTER_INITIAL_CAPACITY);
    }

    private final int capacityExponentIncrease;

    private int capacityExponent;
    private long keyMask;

    protected abstract void rehashWithKeyMask(T hashed, T newHashed, long newCapacity, int capacityExponentIncrease, int newCapacityExponent, long newKeyMask);

    protected BaseLongCapacityExponentArrayHashed(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, CapacityExponents.computeIntCapacityFromExponent(initialOuterCapacityExponent), innerCapacityExponent, loadFactor, getRecreateOuterCapacity(),
                (o, i) -> {

                    Checks.areEqual(o, CapacityExponents.computeIntCapacityFromExponent(initialOuterCapacityExponent));

                    return createHashed.apply(initialOuterCapacityExponent, i);

                }, clearHashed);

        Checks.isIntInitialOuterCapacityExponent(initialOuterCapacityExponent);
        Checks.isIntCapacityExponentIncrease(capacityExponentIncrease);
        Checks.isIntCapacityExponent(initialOuterCapacityExponent + capacityExponentIncrease);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        this.capacityExponentIncrease = capacityExponentIncrease;
        this.capacityExponent = initialOuterCapacityExponent;

        this.keyMask = makeKeyMaskFromCapacityExponent(initialOuterCapacityExponent + innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    protected BaseLongCapacityExponentArrayHashed(AllocationType allocationType, BaseLongCapacityExponentArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        this.capacityExponentIncrease = toCopy.capacityExponentIncrease;

        this.capacityExponent = toCopy.capacityExponent;
        this.keyMask = toCopy.keyMask;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected void rehashWithCapacity(T hashed, T newHashed, long newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }

        final int newCapacityExponentFromNewCapacity = makeCapacityExponent(newCapacity);
        final int newCapacityExponentFromIncrease = capacityExponent + capacityExponentIncrease;

        final int newCapacityExponent = Math.max(newCapacityExponentFromNewCapacity, newCapacityExponentFromIncrease);

        if (DEBUG) {

            debug("computed new capacity", b -> b.add("newCapacityExponent", newCapacityExponent).add("capacityExponent", capacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease));
        }

        if (ASSERT) {

            Assertions.areEqual(newCapacity, CapacityExponents.computeLongCapacityFromExponent(newCapacityExponent));
            Assertions.isGreaterThanOrEqualTo(newCapacityExponent, capacityExponent + capacityExponentIncrease);
        }

        final long newKeyMask = makeKeyMaskFromCapacityExponent(newCapacityExponent);

        rehashWithKeyMask(hashed, newHashed, newCapacity, capacityExponentIncrease, newCapacityExponent, newKeyMask);

        this.capacityExponent = newCapacityExponent;
        this.keyMask = newKeyMask;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    long computeNewCapacityForIncrease(long currentCapacity) {

        Checks.areEqual(currentCapacity, CapacityExponents.computeLongCapacityFromExponent(capacityExponent));

        return CapacityExponents.computeLongCapacityFromExponent(capacityExponent + capacityExponentIncrease);
    }

    @Override
    long adjustNewCapacity(long requiredCapacity) {

        final int capacityExponent = CapacityExponents.computeLongCapacityExponentForAboveZero(requiredCapacity);

        return CapacityExponents.computeLongCapacityFromExponent(capacityExponent);
    }

    protected final int getCapacityExponent() {
        return capacityExponent;
    }

    protected final long getKeyMask() {
        return keyMask;
    }

    protected final long checkCapacityForOneMoreElement() {

        if (DEBUG) {

            enter();
        }

        final long result = checkCapacity(1L);

        if (DEBUG) {

            exitWithHex(result);
        }

        return result;
    }

    protected final long checkCapacity(long numAdditionalElements) {

        Checks.isLongLengthAboveZero(numAdditionalElements);

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements));
        }

        checkCapacity(numAdditionalElements, this, (h, n, c, i) -> i.rehashWithCapacity(h, n, c));

        final long result = keyMask;

        if (DEBUG) {

            exitWithHex(result);
        }

        return result;
    }

    private static int makeCapacityExponent(long capacity) {

        return CapacityExponents.computeLongCapacityExponentForAboveZero(capacity);
    }

    private static long makeKeyMaskFromCapacityExponent(int capacityExponent) {

        return CapacityExponents.makeLongKeyMask(capacityExponent);
    }
}
