package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class LongToObjectMaxDistanceNonBucketMap<V, M extends LongToObjectMaxDistanceNonBucketMap<V, M>> extends BaseLongToObjectMaxDistanceNonBucketMap<V, M> {

    LongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    LongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseLongToObjectMaxDistanceNonBucketMap<V, ?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }

    LongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseLongToObjectMaxDistanceNonBucketMap<V, M> toCopy) {
        super(allocationType, toCopy);
    }
}
