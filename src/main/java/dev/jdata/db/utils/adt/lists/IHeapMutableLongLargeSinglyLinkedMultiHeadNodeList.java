package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongLargeSinglyLinkedMultiHeadNodeList<T> extends IMutableLongLargeSinglyLinkedMultiHeadNodeList<T>, IHeapMultiHeadNodeListMarker {

    public static <T> IHeapMutableLongLargeSinglyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableLongLargeSinglyLinkedMultiHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
