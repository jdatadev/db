package dev.jdata.db.utils.adt.lists;

@Deprecated
public final class IntMultiHeadDoublyLinkedList<T> extends BaseIntDoublyLinkedList<T> {

    public IntMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(new LongMultiHeadDoublyLinkedList<>(initialOuterCapacity, innerCapacity));
    }
}
