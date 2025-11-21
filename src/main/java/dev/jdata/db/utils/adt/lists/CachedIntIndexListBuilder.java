package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.ICacheableMarker;

final class CachedIntIndexListBuilder

        extends IntIndexListBuilder<ICachedIntIndexList, IHeapIntIndexList, MutableIntIndexList>
        implements ICachedIntIndexListBuilder, ICacheableMarker {

    CachedIntIndexListBuilder(AllocationType allocationType, MutableIntIndexListAllocator<?, ? extends MutableIntIndexList> mutableIntIndexListAllocator) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY, mutableIntIndexListAllocator);
    }

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
    protected ICachedIntIndexList withArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return CachedIntIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapIntIndexList withHeapArray(AllocationType allocationType, int[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapIntIndexList.withArray(allocationType, elementsArray, numElements);
    }
}
