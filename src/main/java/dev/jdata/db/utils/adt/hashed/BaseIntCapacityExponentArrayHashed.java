package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseIntCapacityExponentArrayHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES>

        extends BaseIntCapacityArrayHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> {

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

    protected abstract void rehashWithKeyMask(HASHED hashed, HASHED newHashed, int newCapacity, int capacityExponentIncrease, int newKeyMask);

    protected BaseIntCapacityExponentArrayHashed(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<HASHED> createHashed, Consumer<HASHED> clearHashed) {
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

    protected BaseIntCapacityExponentArrayHashed(AllocationType allocationType, BaseIntCapacityExponentArrayHashed<HASHED, ?, ?, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        this.capacityExponentIncrease = toInitializeFrom.capacityExponentIncrease;

        this.capacityExponent = toInitializeFrom.capacityExponent;
        this.keyMask = toInitializeFrom.keyMask;

        if (DEBUG) {

            exit();
        }
    }

    protected BaseIntCapacityExponentArrayHashed(AllocationType allocationType, BaseIntCapacityExponentArrayHashed<HASHED, ?, ?, ?> toCopy, Function<HASHED, HASHED> copyHashed) {
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
    protected void rehashWithCapacity(HASHED hashed, HASHED newHashed, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }

        final int newCapacityExponent = makeCapacityExponent(newCapacity);

        final int exponentIncrease = capacityExponentIncrease;

        if (ASSERT) {

            Assertions.areEqual(newCapacity, CapacityExponents.computeIntCapacityFromExponent(newCapacityExponent));
            Assertions.isGreaterThanOrEqualTo(capacityExponent + exponentIncrease, newCapacityExponent);
        }

        this.capacityExponent = newCapacityExponent;

        final int newKeyMask = this.keyMask = makeKeyMaskFromCapacityExponent(newCapacityExponent);

        rehashWithKeyMask(hashed, newHashed, newCapacity, exponentIncrease, newKeyMask);

        if (DEBUG) {

            exit();
        }
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

    private int getCapacityExponentIncrease() {
        return capacityExponentIncrease;
    }

    private static int makeKeyMask(int capacityExponent) {

        return CapacityExponents.makeIntKeyMask(capacityExponent);
    }

    private static int makeCapacityExponent(long capacity) {

        return CapacityExponents.computeIntCapacityExponentExactForAboveZero(capacity);
    }

    private static int makeKeyMaskFromCapacityExponent(int capacityExponent) {

        return CapacityExponents.makeIntKeyMask(capacityExponent);
    }
}
