package dev.jdata.db.utils.adt.lists;final class HeapMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE>

        extends MutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE>
        implements IHeapMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE> {

    static <T> HeapMutableLongLargeSinglyLinkedMultiHeadNodeList<T> create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        checkCreate(allocationType, initialOuterCapacity, innerCapacity);

        return new HeapMutableLongLargeSinglyLinkedMultiHeadNodeList<>(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableLongLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }

    @Override
    protected INodeListView createEmpty(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        return create(allocationType, initialOuterCapacity, innerCapacity);
    }
}
