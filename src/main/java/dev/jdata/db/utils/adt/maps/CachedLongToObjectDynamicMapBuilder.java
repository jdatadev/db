package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class CachedLongToObjectDynamicMapBuilder<V>

        extends LongToObjectDynamicMapBuilder<V, ICachedLongToObjectDynamicMap<V>, IHeapLongToObjectDynamicMap<V>, CachedMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements ICachedLongToObjectDynamicMapBuilder<V> {

    CachedLongToObjectDynamicMapBuilder(AllocationType allocationType, int initialCapacity,
            CachedMutableLongToObjectDynamicMapAllocator<V> mutableLongToObjectMaxDistanceNonBucketMapAllocator, IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacity, mutableLongToObjectMaxDistanceNonBucketMapAllocator, (t, c, a) -> a.allocateMutableInstance(c));
    }

    private <P> CachedLongToObjectDynamicMapBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<CachedMutableLongToObjectMaxDistanceNonBucketMap<V>, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected ICachedLongToObjectDynamicMap<V> empty() {

        return CachedLongToObjectEmptyDynamicMap.empty();
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> heapEmpty() {

        return HeapLongToObjectEmptyDynamicMap.empty();
    }

    @Override
    protected ICachedLongToObjectDynamicMap<V> withMakeElementsFrom(AllocationType allocationType, CachedMutableLongToObjectMaxDistanceNonBucketMap<V> makeElementsFrom,
            int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return CachedLongToObjectMaxDistanceNonBucketMap.withMakeElementsFrom(allocationType, makeElementsFrom);
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> withHeapMakeElementsFrom(AllocationType allocationType, CachedMutableLongToObjectMaxDistanceNonBucketMap<V> makeElementsFrom,
            int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return HeapLongToObjectMaxDistanceNonBucketMap.withMakeElementsFrom(allocationType, makeElementsFrom);
    }
}
