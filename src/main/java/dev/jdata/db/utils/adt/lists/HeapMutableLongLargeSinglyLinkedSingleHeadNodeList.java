package dev.jdata.db.utils.adt.lists;

final class HeapMutableLongLargeSinglyLinkedSingleHeadNodeList

        extends MutableLongLargeSinglyLinkedSingleHeadNodeList
        implements IHeapMutableLongLargeSinglyLinkedSingleHeadNodeList {

    static HeapMutableLongLargeSinglyLinkedSingleHeadNodeList create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        checkCreate(allocationType, initialOuterCapacity, innerCapacity);

        return new HeapMutableLongLargeSinglyLinkedSingleHeadNodeList(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableLongLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }

    @Override
    protected INodeListView createEmpty(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        return create(allocationType, initialOuterCapacity, innerCapacity);
    }
}
