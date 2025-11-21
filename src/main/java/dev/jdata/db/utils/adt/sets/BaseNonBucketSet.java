package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseNonBucketSet<T> extends BaseArraySet<T, T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_NON_BUCKET_SET;

    private final IntFunction<T> createHashed;

    abstract void rehashElements(T hashArray, T newHashArray, int newKeyMask);

    BaseNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        this.createHashed = Objects.requireNonNull(createHashed);
    }

    BaseNonBucketSet(AllocationType allocationType, BaseNonBucketSet<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        this.createHashed = toCopy.createHashed;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, T, P, R> makeFromElements) {

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

        return makeFromElements.apply(allocationType, createHashed, getHashed(), getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final void rehashWithKeyMask(T hashArray, T newHashArray, int newCapacity, int capacityExponentIncrease, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }

        rehashElements(hashArray, newHashArray, newKeyMask);

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }
    }

    final void clearNonBucketSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
