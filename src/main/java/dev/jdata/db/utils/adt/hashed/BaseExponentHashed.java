package dev.jdata.db.utils.adt.hashed;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public abstract class BaseExponentHashed<T> extends BaseHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_EXPONENT_HASHED;

    private static final Class<?> debugClass = BaseExponentHashed.class;

    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = 2;

    private final int capacityExponentIncrease;

    private int capacityExponent;
    private int keyMask;

    protected BaseExponentHashed(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createHashed);
    }

    protected BaseExponentHashed(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed) {
        super(computeCapacity(initialCapacityExponent), loadFactor, createHashed);

        Checks.isNotNegative(initialCapacityExponent);
        Checks.isAboveZero(capacityExponentIncrease);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacityExponent", capacityExponentIncrease).add("loadFactor", loadFactor).add("createMap", createHashed));
        }

        if (capacityExponentIncrease + capacityExponent >= Integer.SIZE) {

            throw new IllegalArgumentException();
        }

        this.capacityExponentIncrease = capacityExponentIncrease;
        this.capacityExponent = initialCapacityExponent;
        this.keyMask = makeKeyMask(initialCapacityExponent);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    protected final int getKeyMask() {
        return keyMask;
    }

    @Override
    final int increaseCapacity() {

        if (DEBUG) {

            enter();
        }

        final int newCapacityExponent = capacityExponent + capacityExponentIncrease;

        this.capacityExponent = newCapacityExponent;
        this.keyMask = makeKeyMask(newCapacityExponent);

        final int newCapacity = computeCapacity(newCapacityExponent);

        if (DEBUG) {

            exit(newCapacity);
        }

        return newCapacity;
    }

    @Override
    protected final int computeCapacity() {

        return computeCapacity(capacityExponent);
    }

    private static int makeKeyMask(int capacityExponent) {

        return (1 << capacityExponent) - 1;
    }

    private static int computeCapacity(int capacityExponent) {

        Checks.isCapacityExponent(capacityExponent);

        return 1 << capacityExponent;
    }

    protected static int computeCapacityExponent(int numElements, float loadFactor) {

        int capacityExponent;

        for (capacityExponent = 0; shouldRehash(numElements, computeCapacity(capacityExponent), loadFactor); ++ capacityExponent) {

        }

        return capacityExponent;
    }
}
