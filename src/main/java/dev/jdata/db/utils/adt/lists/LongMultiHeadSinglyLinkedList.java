package dev.jdata.db.utils.adt.lists;

public final class LongMultiHeadSinglyLinkedList<T> extends BaseLongSinglyLinkedList<T> {

    public LongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    @Override
    public boolean isEmpty() {

        throw new UnsupportedOperationException();
    }
}
