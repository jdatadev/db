package dev.jdata.db.utils.adt.lists;

public final class LargeLongMultiHeadSinglyLinkedList<INSTANCE>

        extends BaseLargeLongMultiHeadSinglyLinkedList<INSTANCE, LargeLongMultiHeadSinglyLinkedList<INSTANCE>, LongValues<LargeLongMultiHeadSinglyLinkedList<INSTANCE>>> {

    public LargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongValues::new);
    }
}
