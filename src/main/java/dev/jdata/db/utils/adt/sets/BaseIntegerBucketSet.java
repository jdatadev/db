package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.LargeNodeLists;

abstract class BaseIntegerBucketSet<T, U extends BaseIntegerBucketSet<T, U>> extends BaseIntegerSet<T, U> {

    static final long NO_LONG_NODE = LargeNodeLists.NO_LONG_NODE;

    BaseIntegerBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerBucketSet(AllocationType allocationType, BaseIntegerBucketSet<T, U> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
