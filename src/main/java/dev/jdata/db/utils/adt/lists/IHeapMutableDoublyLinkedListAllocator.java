package dev.jdata.db.utils.adt.lists;

public interface IHeapMutableDoublyLinkedListAllocator<T> extends IMutableDoublyLinkedListAllocator<T, IHeapMutableDoublyLinkedList<T>> {

    public static <T> IHeapMutableDoublyLinkedListAllocator<T> create() {

        return new HeapMutableDoublyLinkedListAllocator<>();
    }
}
