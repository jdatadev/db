package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

@Deprecated // necessary?
abstract class BaseLongArrayKeysNonBucketMap<VALUES, MAP extends BaseLongArrayKeysNonBucketMap<VALUES, MAP>> extends BaseLongKeyNonBucketMap<VALUES, MAP> {

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, BaseLongArrayKeysNonBucketMap<VALUES, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);
    }

    BaseLongArrayKeysNonBucketMap(AllocationType allocationType, BaseLongArrayKeysNonBucketMap<VALUES, ?> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);
    }
}
