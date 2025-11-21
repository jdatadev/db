package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntLargeSinglyLinkedSingleHeadNodeList extends IMutableIntLargeSinglyLinkedSingleHeadNodeList, IHeapSingleHeadNodeListMarker {

    public static IHeapMutableIntLargeDoublyLinkedSingleHeadNodeList create(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableIntLargeDoublyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
