package dev.jdata.db.utils.adt.sets;

final class HeapIntSetBuilder extends IntSetBuilder<IHeapIntSet, IHeapIntSet, MutableIntMaxDistanceNonBucketSet> implements IHeapIntSetBuilder {

    HeapIntSetBuilder(AllocationType allocationType, int initialCapacity) {
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
    protected IHeapIntSet withArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected IHeapIntSet withHeapArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return withArray(allocationType, elementsArray, numElements);
    }
}
