package dev.jdata.db.utils.adt.lists;

public interface IMutableLongLargeDoublyLinkedMultiHeadNodeList<T> extends IMutableLongDoublyLinkedMultiHeadNodeList<T> {

    public static <T> IMutableLongLargeDoublyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return new MutableLongLargeDoublyLinkedMultiHeadNodeList<>(initialOuterCapacity, innerCapacity);
    }
}
