package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongLargeSinglyLinkedSingleHeadNodeList extends IMutableLongLargeSinglyLinkedSingleHeadNodeList, IHeapSingleHeadNodeListMarker {

    public static IHeapMutableLongLargeSinglyLinkedSingleHeadNodeList create(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableLongLargeSinglyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
