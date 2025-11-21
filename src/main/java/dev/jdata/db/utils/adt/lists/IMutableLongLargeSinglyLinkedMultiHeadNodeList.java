package dev.jdata.db.utils.adt.lists;

public interface IMutableLongLargeSinglyLinkedMultiHeadNodeList<T> extends IBaseMutableLongSinglyLinkedMultiHeadNodeList<T> {

    public static <T> IMutableLongLargeSinglyLinkedMultiHeadNodeList<T> create(int initialOuterCapacity, int innerCapacity) {

        return new MutableLongLargeSinglyLinkedMultiHeadNodeList<>(initialOuterCapacity, innerCapacity);
    }
}
