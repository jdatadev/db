package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

@Deprecated // necessary?
abstract class BaseIntArrayKeysNonBucketMap<VALUES, MAP extends BaseIntArrayKeysNonBucketMap<VALUES, MAP>> extends BaseIntKeyNonBucketMap<VALUES, MAP> {

    BaseIntArrayKeysNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntArrayKeysNonBucketMap(AllocationType allocationType, BaseIntArrayKeysNonBucketMap<VALUES, MAP> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);
    }
}
