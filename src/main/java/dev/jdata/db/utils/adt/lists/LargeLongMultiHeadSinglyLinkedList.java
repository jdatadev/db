package dev.jdata.db.utils.adt.lists;

public final class LargeLongMultiHeadSinglyLinkedList<T> extends BaseLargeLongSinglyLinkedList<T> {

    public LargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }
}
