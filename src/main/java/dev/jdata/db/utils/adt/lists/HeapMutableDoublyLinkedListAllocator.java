package dev.jdata.db.utils.adt.lists;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.LongCapacityObjectHeapMutableInstanceAllocator;

final class HeapMutableDoublyLinkedListAllocator<T>

        extends LongCapacityObjectHeapMutableInstanceAllocator<IHeapMutableDoublyLinkedList<T>, HeapMutableObjectDoublyLinkedList<T>, Node<T>, IObjectIterableElementsView<T>>
        implements IHeapMutableDoublyLinkedListAllocator<T> {

    @Override
    public IHeapMutableDoublyLinkedList<T> copyToMutable(IObjectIterableElementsView<T> mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }

    @Override
    protected HeapMutableObjectDoublyLinkedList<T> allocateMutable(long minimumCapacity, LongFunction<Node<T>> createElements) {

        checkAllocateMutableWithoutCreateElementsParameters(minimumCapacity, createElements);

        return HeapMutableObjectDoublyLinkedList.create(AllocationType.HEAP_ALLOCATOR);
    }
}
