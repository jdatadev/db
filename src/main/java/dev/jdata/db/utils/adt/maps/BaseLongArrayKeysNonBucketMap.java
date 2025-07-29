package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

abstract class BaseLongArrayKeysNonBucketMap<T> extends BaseLongKeyNonBucketMap<T> {

    BaseLongArrayKeysNonBucketMap(int initialCapacityExponent, IntFunction<T> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongArrayKeysNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongArrayKeysNonBucketMap(BaseLongArrayKeysNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, copyValuesContent);
    }
}
