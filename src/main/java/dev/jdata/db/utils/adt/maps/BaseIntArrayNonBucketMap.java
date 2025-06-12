package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;

@Deprecated // necessary?
abstract class BaseIntArrayNonBucketMap<T> extends BaseIntKeyNonBucketMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_ARRAY_MAP;

    BaseIntArrayNonBucketMap(int initialCapacityExponent, IntFunction<T> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseIntArrayNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntArrayNonBucketMap(BaseIntArrayNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, copyValuesContent);
    }
}
