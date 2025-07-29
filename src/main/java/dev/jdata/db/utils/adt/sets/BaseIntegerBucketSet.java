package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.LargeLists;

abstract class BaseIntegerBucketSet<T> extends BaseIntegerSet<T> {

    static final long NO_LONG_NODE = LargeLists.NO_LONG_NODE;

    BaseIntegerBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerBucketSet(BaseIntegerBucketSet<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);
    }
}
