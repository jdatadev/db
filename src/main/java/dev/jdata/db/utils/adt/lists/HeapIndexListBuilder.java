package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

final class HeapIndexListBuilder<T>

        extends IndexListBuilder<T, IHeapIndexList<T>, HeapMutableIndexList<T>, IHeapIndexList<T>, IHeapIndexListBuilder<T>, HeapIndexListAllocator<T>> {

    HeapIndexListBuilder(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        this(allocationType, initialCapacity, new HeapIndexListAllocator<>(createElementsArray));
    }

    HeapIndexListBuilder(AllocationType allocationType, int initialCapacity, HeapIndexListAllocator<T> listAllocator) {
        super(allocationType, initialCapacity, listAllocator);
    }

    @Override
    protected IHeapIndexList<T> build() {

        return fromMutableIndexListHeap();
    }

    @Override
    protected HeapIndexList<T> empty() {

        return HeapIndexList.empty();
    }

    @Override
    protected IHeapIndexList<T> heapBuild() {

        return fromMutableIndexListHeap();
    }

    @Override
    protected HeapIndexList<T> heapEmpty() {

        return HeapIndexList.empty();
    }

    @Override
    protected final HeapIndexList<T> fromMutableHeap(AllocationType allocationType) {

        return HeapIndexList.fromMutableIndexList(allocationType, getMutable());
    }

    private HeapIndexList<T> fromMutableIndexListHeap() {

        return fromMutableHeap(AllocationType.HEAP_ALLOCATOR);
    }
}
