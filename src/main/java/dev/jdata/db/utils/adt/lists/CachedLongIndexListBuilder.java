package dev.jdata.db.utils.adt.lists;

final class CachedLongIndexListBuilder extends LongIndexListBuilder<ICachedLongIndexList, IHeapLongIndexList, MutableLongIndexList> implements ICachedLongIndexListBuilder {

    @Deprecated // fix allocateMutableElements() ?
    CachedLongIndexListBuilder(AllocationType allocationType, int initialCapacity,
            MutableLongIndexListAllocator<?, ? extends MutableLongIndexList> mutableLongIndexListAllocator) {
        this(allocationType, initialCapacity, mutableLongIndexListAllocator, (t, c, a) -> a.allocateMutableInstance(c));
    }

    <P> CachedLongIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableLongIndexList, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected ICachedLongIndexList empty() {

        return CachedLongIndexList.empty();
    }

    @Override
    protected IHeapLongIndexList heapEmpty() {

        return HeapLongIndexList.empty();
    }

    @Override
    protected ICachedLongIndexList withMakeElementsFrom(AllocationType allocationType, long[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return CachedLongIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }

    @Override
    protected IHeapLongIndexList withHeapMakeElementsFrom(AllocationType allocationType, long[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapLongIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }
}
