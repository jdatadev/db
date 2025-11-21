package dev.jdata.db.utils.adt.lists;

final class HeapLongIndexListBuilder

        extends LongIndexListBuilder<IHeapLongIndexList, MutableLongIndexList, IHeapLongIndexList, IHeapLongIndexListBuilder>
        implements IHeapLongIndexListBuilder {

    HeapLongIndexListBuilder(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    HeapLongIndexListBuilder(AllocationType allocationType, int initialCapacity) {
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
    protected IHeapLongIndexList withArray(AllocationType allocationType, long[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapLongIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapLongIndexList withHeapArray(AllocationType allocationType, long[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return withArray(allocationType, elementsArray, numElements);
    }
}
