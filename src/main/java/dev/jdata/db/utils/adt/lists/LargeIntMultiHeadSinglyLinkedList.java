package dev.jdata.db.utils.adt.lists;

public final class LargeIntMultiHeadSinglyLinkedList<INSTANCE>

        extends BaseLargeIntMultiHeadSinglyLinkedList<INSTANCE, LargeIntMultiHeadSinglyLinkedList<INSTANCE>, IntValues<LargeIntMultiHeadSinglyLinkedList<INSTANCE>>> {

    public LargeIntMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntValues::new);
    }
}
