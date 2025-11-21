package dev.jdata.db.utils.adt.lists;

final class HeapIntIndexListBuilder extends IntIndexListBuilder<IHeapIntIndexList, IHeapIntIndexList, MutableIntIndexList> implements IHeapIntIndexListBuilder {

    static HeapIntIndexListBuilder create(AllocationType allocationType) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapIntIndexListBuilder(allocationType);
    }

    static HeapIntIndexListBuilder create(AllocationType allocationType, int initialCapacity) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapIntIndexListBuilder(allocationType, initialCapacity);
    }

    private HeapIntIndexListBuilder(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    private HeapIntIndexListBuilder(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, HeapMutableIntIndexListAllocator.INSTANCE);
    }

    private HeapIntIndexListBuilder(AllocationType allocationType, int initialCapacity,
            HeapMutableIntIndexListAllocator mutableIntIndexListAllocator) {
        this(allocationType, initialCapacity, mutableIntIndexListAllocator, (t, c, a) -> a.allocateMutable(c));
    }

    private <P> HeapIntIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableIntIndexList, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapIntIndexList empty() {

        return HeapIntIndexList.empty();
    }

    @Override
    protected IHeapIntIndexList heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapIntIndexList withMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapIntIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }

    @Override
    protected IHeapIntIndexList withHeapMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapIntIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }
}
