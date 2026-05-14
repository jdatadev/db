package dev.jdata.db.utils.adt.lists;

final class CachedIntIndexListBuilder extends IntIndexListBuilder<ICachedIntIndexList, IHeapIntIndexList, MutableIntIndexList> implements ICachedIntIndexListBuilder {

    CachedIntIndexListBuilder(AllocationType allocationType, int initialCapacity,
            MutableIntIndexListAllocator<?, ? extends MutableIntIndexList> mutableIntIndexListAllocator) {
        this(allocationType, initialCapacity, mutableIntIndexListAllocator, (t, c, a) -> a.allocateMutableInstance(c));
    }

    <P> CachedIntIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableIntIndexList, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected ICachedIntIndexList empty() {

        return CachedIntIndexList.empty();
    }

    @Override
    protected IHeapIntIndexList heapEmpty() {

        return HeapIntIndexList.empty();
    }

    @Override
    protected ICachedIntIndexList withMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return CachedIntIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }

    @Override
    protected IHeapIntIndexList withHeapMakeElementsFrom(AllocationType allocationType, int[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapIntIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }
}
