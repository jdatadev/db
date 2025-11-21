package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

abstract class BaseLongArrayKeysNonBucketMap<VALUES, MAP extends BaseLongArrayKeysNonBucketMap<VALUES, MAP>> extends BaseLongKeyNonBucketMap<VALUES, MAP> {

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<VALUES> createValuesArray) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, BaseLongArrayKeysNonBucketMap<VALUES, MAP> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);
    }
}
