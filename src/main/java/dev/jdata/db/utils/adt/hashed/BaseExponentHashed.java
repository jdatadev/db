package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public abstract class BaseExponentHashed<T> extends BaseHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_EXPONENT_HASHED;

    private static final Class<?> debugClass = BaseExponentHashed.class;

    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = 2;

    private final int capacityExponentIncrease;

    private int capacityExponent;
    private int keyMask;

    protected BaseExponentHashed(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createHashed, clearHashed);
    }

    protected BaseExponentHashed(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(CapacityExponents.computeCapacity(initialCapacityExponent), loadFactor, createHashed, clearHashed);

        Checks.isNotNegative(initialCapacityExponent);
        Checks.isAboveZero(capacityExponentIncrease);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacityExponent", capacityExponentIncrease).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed));
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

        final int newCapacity = CapacityExponents.computeCapacity(newCapacityExponent);

        if (DEBUG) {

            exit(newCapacity);
        }

        return newCapacity;
    }

    @Override
    protected final int computeCapacity() {

        return CapacityExponents.computeCapacity(capacityExponent);
    }

    private static int makeKeyMask(int capacityExponent) {

        return (1 << capacityExponent) - 1;
    }

    protected static int computeCapacityExponent(int numElements, float loadFactor) {

        int capacityExponent;

        for (capacityExponent = 0; shouldRehash(numElements, CapacityExponents.computeCapacity(capacityExponent), loadFactor); ++ capacityExponent) {

        }

        return capacityExponent;
    }
}
