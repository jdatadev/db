package dev.jdata.db.utils.adt.lists;

final class HeapMutableLongLargeDoublyLinkedSingleHeadNodeList

        extends MutableLongLargeDoublyLinkedSingleHeadNodeList
        implements IHeapMutableLongLargeDoublyLinkedSingleHeadNodeList {

    static HeapMutableLongLargeDoublyLinkedSingleHeadNodeList create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        return new HeapMutableLongLargeDoublyLinkedSingleHeadNodeList(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableLongLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }
}
