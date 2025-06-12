package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;

@Deprecated // necessary ?
abstract class BaseLongArrayNonBucketMap<T> extends BaseLongKeyNonBucketMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_NON_BUCKET_ARRAY_MAP;

    BaseLongArrayNonBucketMap(int initialCapacityExponent, IntFunction<T> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongArrayNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongArrayNonBucketMap(BaseLongArrayNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, copyValuesContent);
    }
}
