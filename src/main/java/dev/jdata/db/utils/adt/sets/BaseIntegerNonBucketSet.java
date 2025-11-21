package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseIntegerNonBucketSet<T> extends BaseIntegerSet<T, T> {

    private final IntFunction<T> createHashed;

    BaseIntegerNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        this.createHashed = Objects.requireNonNull(createHashed);
    }

    BaseIntegerNonBucketSet(AllocationType allocationType, BaseIntegerNonBucketSet<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        this.createHashed = toCopy.createHashed;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, T, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, createHashed, getHashed(), getMakeFromElementsNumElements(), parameter);
    }
}
