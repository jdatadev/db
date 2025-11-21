package dev.jdata.db.utils.adt.maps;

final class HeapLongToIntStaticMapBuilder

        extends LongToIntStaticMapBuilder<IHeapLongToIntStaticMap, IHeapLongToIntStaticMap, HeapMutableLongToIntNonRemoveNonBucketMap>
        implements IHeapLongToIntStaticMapBuilder {

    private HeapLongToIntStaticMapBuilder(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    private HeapLongToIntStaticMapBuilder(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, HeapMutableLongToIntNonRemoveNonBucketMapAllocator.INSTANCE);
    }

    private HeapLongToIntStaticMapBuilder(AllocationType allocationType, int initialCapacity,
            HeapMutableLongToIntNonRemoveNonBucketMapAllocator mutableLongToIntNonRemoveNonBucketMapAllocator) {
        this(allocationType, initialCapacity, mutableLongToIntNonRemoveNonBucketMapAllocator, (t, c, a) -> a.allocateMutable(c));
    }

    private <P> HeapLongToIntStaticMapBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<HeapMutableLongToIntNonRemoveNonBucketMap, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapLongToIntStaticMap empty() {

        return HeapLongToIntEmptyStaticMap.empty();
    }

    @Override
    protected IHeapLongToIntStaticMap heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapLongToIntStaticMap withMakeElementsFrom(AllocationType allocationType, HeapMutableLongToIntNonRemoveNonBucketMap makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return HeapLongToIntNonContainsKeyNonBucketMap.withMakeElementsFrom(allocationType, makeElementsFrom);
    }

    @Override
    protected IHeapLongToIntStaticMap withHeapMakeElementsFrom(AllocationType allocationType, HeapMutableLongToIntNonRemoveNonBucketMap makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);

        return withMakeElementsFrom(allocationType, makeElementsFrom, numElements);
    }
}
