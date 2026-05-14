package dev.jdata.db.utils.adt.maps;

final class CachedLongToObjectMaxDistanceNonBucketMap<V>

        extends LongToObjectMaxDistanceNonBucketMap<V, CachedLongToObjectMaxDistanceNonBucketMap<V>>
        implements ICachedLongToObjectDynamicMap<V> {

    static <V> ICachedLongToObjectDynamicMap<V> empty() {

        return CachedLongToObjectEmptyDynamicMap.empty();
    }

    @Deprecated // check parameters
    static <V> CachedLongToObjectMaxDistanceNonBucketMap<V> createEmptyValuesInitializable(AllocationType allocationType) {

        AllocationType.checkIsCached(allocationType);

        throw new UnsupportedOperationException();
    }

    static <V> CachedLongToObjectMaxDistanceNonBucketMap<V> withMakeElementsFrom(AllocationType allocationType,
            MutableLongToObjectMaxDistanceNonBucketMap<V, ?> toInitializeFrom) {

        checkWithMakeElementsFrom(allocationType, AllocationMechanism.HEAP, toInitializeFrom);

        return new CachedLongToObjectMaxDistanceNonBucketMap<>(allocationType, toInitializeFrom, null);
    }

    private CachedLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseLongToObjectMaxDistanceNonBucketMap<V, ?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }
}
