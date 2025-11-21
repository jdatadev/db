package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IMutableIntLargeSinglyLinkedMultiHeadNodeList<T> extends IMutableIntSinglyLinkedMultiHeadNodeList<T> {

    public static <T> IMutableIntLargeSinglyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return new MutableIntLargeSinglyLinkedMultiHeadNodeList<>(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
