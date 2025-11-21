package dev.jdata.db.utils.adt.lists;

final class HeapMutableIntLargeSinglyLinkedSingleHeadNodeList

        extends MutableIntLargeSinglyLinkedSingleHeadNodeList
        implements IHeapMutableIntLargeSinglyLinkedSingleHeadNodeList {

    static HeapMutableIntLargeSinglyLinkedSingleHeadNodeList create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        checkCreate(allocationType, initialOuterCapacity, innerCapacity);

        return new HeapMutableIntLargeSinglyLinkedSingleHeadNodeList(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableIntLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }

    @Override
    protected INodeListView createEmpty(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        return create(allocationType, initialOuterCapacity, innerCapacity);
    }
}
