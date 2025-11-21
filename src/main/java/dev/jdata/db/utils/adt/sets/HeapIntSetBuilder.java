package dev.jdata.db.utils.adt.sets;

final class HeapIntSetBuilder extends IntSetBuilder<IHeapIntSet, IHeapIntSet, MutableIntMaxDistanceNonBucketSet> implements IHeapIntSetBuilder {

    static HeapIntSetBuilder create(AllocationType allocationType, int initialCapacity) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapIntSetBuilder(allocationType, initialCapacity);
    }

    private HeapIntSetBuilder(AllocationType allocationType, int initialCapacity) {
        this(allocationType, initialCapacity, HeapMutableIntSetAllocator.INSTANCE);
    }

    private HeapIntSetBuilder(AllocationType allocationType, int initialCapacity, HeapMutableIntSetAllocator mutableIntSetAllocator) {
        this(allocationType, initialCapacity, mutableIntSetAllocator, (t, c, a) -> a.allocateMutable(c));
    }

    private <P> HeapIntSetBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableIntMaxDistanceNonBucketSet, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapIntSet empty() {

        return HeapIntMaxDistanceNonBucketSet.empty();
    }

    @Override
    protected IHeapIntSet heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapIntSet withMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected IHeapIntSet withHeapMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return withMakeElementsFrom(allocationType, makeElementsFrom, numElements);
    }
}
