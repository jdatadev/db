package dev.jdata.db.utils.adt.lists;

final class HeapIntIndexListBuilder extends IntIndexListBuilder<IHeapIntIndexList, IHeapIntIndexList, MutableIntIndexList> implements IHeapIntIndexListBuilder {

    HeapIntIndexListBuilder(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    HeapIntIndexListBuilder(AllocationType allocationType, int initialCapacity) {
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
    protected IHeapIntIndexList withArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapIntIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapIntIndexList withHeapArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapIntIndexList.withArray(allocationType, elementsArray, numElements);
    }
}
