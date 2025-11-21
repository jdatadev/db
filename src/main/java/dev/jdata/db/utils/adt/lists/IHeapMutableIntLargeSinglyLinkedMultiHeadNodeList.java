package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntLargeSinglyLinkedMultiHeadNodeList<T> extends IMutableIntLargeSinglyLinkedMultiHeadNodeList<T>, IHeapMultiHeadNodeListMarker {

    public static <T> IHeapMutableIntLargeSinglyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableIntLargeSinglyLinkedMultiHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
