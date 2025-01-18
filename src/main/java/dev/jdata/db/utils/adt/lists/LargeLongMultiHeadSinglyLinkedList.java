package dev.jdata.db.utils.adt.lists;

@Deprecated // currently not in use
public final class LargeLongMultiHeadSinglyLinkedList<T> extends BaseLargeLongSinglyLinkedList<T> {

    public LargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }
}
