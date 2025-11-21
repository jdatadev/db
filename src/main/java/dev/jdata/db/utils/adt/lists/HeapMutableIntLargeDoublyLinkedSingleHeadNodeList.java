package dev.jdata.db.utils.adt.lists;

final class HeapMutableIntLargeDoublyLinkedSingleHeadNodeList

        extends MutableIntLargeDoublyLinkedSingleHeadNodeList
        implements IHeapMutableIntLargeDoublyLinkedSingleHeadNodeList {

    static HeapMutableIntLargeDoublyLinkedSingleHeadNodeList create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        checkCreate(allocationType, initialOuterCapacity, innerCapacity);

        return new HeapMutableIntLargeDoublyLinkedSingleHeadNodeList(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableIntLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }
}
