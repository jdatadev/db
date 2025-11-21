package dev.jdata.db.utils.adt.lists;

final class HeapLongIndexListBuilder extends LongIndexListBuilder<IHeapLongIndexList, IHeapLongIndexList, MutableLongIndexList> implements IHeapLongIndexListBuilder {

    static HeapLongIndexListBuilder create(AllocationType allocationType) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapLongIndexListBuilder(allocationType);
    }

    static HeapLongIndexListBuilder create(AllocationType allocationType, int initialCapacity) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapLongIndexListBuilder(allocationType, initialCapacity);
    }

    private HeapLongIndexListBuilder(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    private HeapLongIndexListBuilder(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, HeapMutableLongIndexListAllocator.INSTANCE);
    }

    private HeapLongIndexListBuilder(AllocationType allocationType, int initialCapacity,
            HeapMutableLongIndexListAllocator mutableLongIndexListAllocator) {
        this(allocationType, initialCapacity, mutableLongIndexListAllocator, (t, c, a) -> a.allocateMutable(c));
    }

    private <P> HeapLongIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableLongIndexList, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapLongIndexList empty() {

        return HeapLongIndexList.empty();
    }

    @Override
    protected IHeapLongIndexList heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapLongIndexList withMakeElementsFrom(AllocationType allocationType, long[] elementsArray, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapLongIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapLongIndexList withHeapMakeElementsFrom(AllocationType allocationType, long[] elementsArray, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return withMakeElementsFrom(allocationType, elementsArray, numElements);
    }
}
