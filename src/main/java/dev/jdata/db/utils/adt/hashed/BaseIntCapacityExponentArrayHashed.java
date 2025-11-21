package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseIntCapacityExponentArrayHashed<T, U, V> extends BaseIntCapacityArrayHashed<T, U, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_EXPONENT_ARRAY_HASHED;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_CAPACITY_EXPONENT_ARRAY_HASHED;

    protected static final int DEFAULT_INITIAL_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INITIAL_CAPACITY_EXPONENT;
    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = HashedConstants.DEFAULT_CAPACITY_EXPONENT_INCREASE;

    private final int capacityExponentIncrease;

    private int capacityExponent;
    private int keyMask;

    private static int getRecreateCapacity() {

        return CapacityExponents.computeIntCapacityFromExponent(DEFAULT_INITIAL_CAPACITY_EXPONENT);
    }

    protected abstract T rehash(T hashed, int newCapacity, int newCapacityExponent, int newKeyMask);

    protected BaseIntCapacityExponentArrayHashed(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, CapacityExponents.computeIntCapacityFromExponent(initialCapacityExponent), loadFactor, getRecreateCapacity(), createHashed, clearHashed,
                createHashed);

        Checks.isIntInitialCapacityExponent(initialCapacityExponent);
        Checks.isIntCapacityExponentIncrease(capacityExponentIncrease);
        Checks.isIntCapacityExponent(initialCapacityExponent + capacityExponentIncrease);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        this.capacityExponentIncrease = capacityExponentIncrease;
        this.capacityExponent = initialCapacityExponent;

        this.keyMask = makeKeyMask(initialCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    protected BaseIntCapacityExponentArrayHashed(AllocationType allocationType, BaseIntCapacityExponentArrayHashed<T, U, V> toCopy, Function<T, T> copyHashed) {
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

    private int getCapacityExponent() {
        return capacityExponent;
    }

    @Override
    protected final T rehash(T hashed, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newCapacity", newCapacity));
        }

        final int newCapacityExponent = makeCapacityExponent(newCapacity);

        if (ASSERT) {

            Assertions.areEqual(newCapacity, CapacityExponents.computeIntCapacityFromExponent(newCapacityExponent));
            Assertions.isGreaterThanOrEqualTo(capacityExponent + capacityExponentIncrease, newCapacityExponent);
        }

        this.capacityExponent = newCapacityExponent;

        final int newKeyMask = this.keyMask = makeKeyMaskFromCapacityExponent(newCapacityExponent);

        final T result = rehash(hashed, newCapacity, newCapacityExponent, newKeyMask);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    final long computeNewCapacityForIncrease(long currentCapacity) {

        if (DEBUG) {

            enter(b -> b.add("currentCapacity", currentCapacity));
        }

        Checks.areEqual(currentCapacity, CapacityExponents.computeIntCapacityFromExponent(capacityExponent));

        final long result = CapacityExponents.computeIntCapacityFromExponent(capacityExponent + capacityExponentIncrease);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    final long adjustNewCapacity(long requiredCapacity) {

        if (DEBUG) {

            enter(b -> b.add("requiredCapacity", requiredCapacity));
        }

        final int capacityExponent = makeCapacityExponent(requiredCapacity);

        final long result = CapacityExponents.computeIntCapacityFromExponent(capacityExponent);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final int getKeyMask() {
        return keyMask;
    }

    protected final int getCapacityExponentIncrease() {
        return capacityExponentIncrease;
    }

    private static int makeKeyMask(int capacityExponent) {

        return CapacityExponents.makeIntKeyMask(capacityExponent);
    }

    private static int makeCapacityExponent(long capacity) {

        return CapacityExponents.computeIntCapacityExponentExact(capacity);
    }

    private static int makeKeyMaskFromCapacityExponent(int capacityExponent) {

        return CapacityExponents.makeIntKeyMask(capacityExponent);
    }
}
