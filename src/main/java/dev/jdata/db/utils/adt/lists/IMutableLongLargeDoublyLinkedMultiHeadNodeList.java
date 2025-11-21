package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IMutableLongLargeDoublyLinkedMultiHeadNodeList<T> extends IMutableLongDoublyLinkedMultiHeadNodeList<T> {

    public static <T> IMutableLongLargeDoublyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return new MutableLongLargeDoublyLinkedMultiHeadNodeList<>(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
