package dev.jdata.db.utils.adt.hashed;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public abstract class BaseIntCapacityExponentHashed<T> extends BaseIntCapacityArrayHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_EXPONENT_HASHED;

    private static final Class<?> debugClass = BaseIntCapacityExponentHashed.class;

    protected static final int DEFAULT_CAPACITY_EXPONENT_INCREASE = 2;

    private final int capacityExponentIncrease;

    private int capacityExponent;
    private int keyMask;

    protected abstract T rehash(T hashed, int newCapacity, int newKeyMask);

    protected BaseIntCapacityExponentHashed(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createHashed, clearHashed);
    }

    protected BaseIntCapacityExponentHashed(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(CapacityExponents.computeCapacity(initialCapacityExponent), loadFactor, createHashed, clearHashed);

        Checks.isInitialCapacityExponent(initialCapacityExponent);
        Checks.isCapacityExponentIncrease(capacityExponentIncrease);
        Checks.isCapacityExponent(capacityExponent + capacityExponentIncrease);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacityExponent", capacityExponentIncrease).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed));
        }

        this.capacityExponentIncrease = capacityExponentIncrease;
        this.capacityExponent = initialCapacityExponent;
        this.keyMask = makeKeyMask(initialCapacityExponent);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    protected BaseIntCapacityExponentHashed(BaseIntCapacityExponentHashed<T> toCopy, BiConsumer<T, T> copyHashedContent) {
        super(toCopy, copyHashedContent);

        this.capacityExponentIncrease = toCopy.capacityExponentIncrease;
        this.capacityExponent = toCopy.capacityExponent;

        this.keyMask = toCopy.keyMask;
    }

    @Override
    protected final T rehash(T hashed, int newCapacity) {

        return rehash(hashed, newCapacity, keyMask);
    }

    protected final int getKeyMask() {
        return keyMask;
    }

    @Override
    final int increaseCapacity() {

        if (DEBUG) {

            enter(b -> b.add("capacityExponent", capacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).hex("keyMask", keyMask));
        }

        final int newCapacityExponent = capacityExponent + capacityExponentIncrease;

        this.capacityExponent = newCapacityExponent;
        this.keyMask = makeKeyMask(newCapacityExponent);

        final int newCapacity = CapacityExponents.computeCapacity(newCapacityExponent);

        if (DEBUG) {

            exit(newCapacity, b -> b.add("capacityExponent", capacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).hex("keyMask", keyMask));
        }

        return newCapacity;
    }

    public final int getCapacityExponent() {
        return capacityExponent;
    }

    @Override
    protected final int computeCapacity() {

        return CapacityExponents.computeCapacity(capacityExponent);
    }

    private static int makeKeyMask(int capacityExponent) {

        return CapacityExponents.makeIntKeyMask(capacityExponent);
    }

    protected static int computeCapacityExponent(int numElements, float loadFactor) {

        int capacityExponent;

        for (capacityExponent = 0; shouldRehash(numElements, CapacityExponents.computeCapacity(capacityExponent), loadFactor); ++ capacityExponent) {

        }

        return capacityExponent;
    }
}
