package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

final class HeapLongToObjectDynamicMapBuilder<V>

        extends LongToObjectDynamicMapBuilder<V, IHeapLongToObjectDynamicMap<V>, IHeapLongToObjectDynamicMap<V>, HeapMutableLongToObjectMaxDistanceNonBucketMap<V>>
        implements IHeapLongToObjectDynamicMapBuilder<V> {

    static <V> HeapLongToObjectDynamicMapBuilder<V> create(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);
        Objects.requireNonNull(createValuesArray);

        return new HeapLongToObjectDynamicMapBuilder<>(allocationType, initialCapacity, createValuesArray);
    }

    private HeapLongToObjectDynamicMapBuilder(AllocationType allocationType, IntFunction<V[]> createValuesArray) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY, createValuesArray);
    }

    private HeapLongToObjectDynamicMapBuilder(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacity, HeapMutableLongToObjectDynamicMapAllocator.create(createValuesArray), createValuesArray);
    }

    private HeapLongToObjectDynamicMapBuilder(AllocationType allocationType, int initialCapacity,
            HeapMutableLongToObjectDynamicMapAllocator<V> mutableLongToObjectMaxDistanceNonBucketMapAllocator, IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacity, mutableLongToObjectMaxDistanceNonBucketMapAllocator, (t, c, a) -> a.allocateMutable(createValuesArray, c));
    }

    private <P> HeapLongToObjectDynamicMapBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<HeapMutableLongToObjectMaxDistanceNonBucketMap<V>, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> empty() {

        return HeapLongToObjectEmptyDynamicMap.empty();
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> withMakeElementsFrom(AllocationType allocationType, HeapMutableLongToObjectMaxDistanceNonBucketMap<V> makeElementsFrom,
            int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return HeapLongToObjectMaxDistanceNonBucketMap.withMakeElementsFrom(allocationType, makeElementsFrom);
    }

    @Override
    protected IHeapLongToObjectDynamicMap<V> withHeapMakeElementsFrom(AllocationType allocationType, HeapMutableLongToObjectMaxDistanceNonBucketMap<V> makeElementsFrom,
            int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return withMakeElementsFrom(allocationType, makeElementsFrom, numElements);
    }
}
