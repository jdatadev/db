package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseIntegerNonBucketSet<T> extends BaseIntegerSet<T> {

    BaseIntegerNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerNonBucketSet(BaseIntegerSet<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);
    }
}
