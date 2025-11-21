package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongLargeDoublyLinkedSingleHeadNodeList extends IMutableLongLargeDoublyLinkedSingleHeadNodeList, IHeapSingleHeadNodeListMarker {

    public static IHeapMutableLongLargeDoublyLinkedSingleHeadNodeList create(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableLongLargeDoublyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
