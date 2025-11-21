package dev.jdata.db.utils.adt.lists;

final class HeapMutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE>

        extends MutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE>
        implements IHeapMutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE> {

    static <T> HeapMutableIntLargeSinglyLinkedMultiHeadNodeList<T> create(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        checkCreate(allocationType, initialOuterCapacity, innerCapacity);

        return new HeapMutableIntLargeSinglyLinkedMultiHeadNodeList<>(allocationType, initialOuterCapacity, innerCapacity);
    }

    private HeapMutableIntLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity);
    }

    @Override
    protected INodeListView createEmpty(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

        return create(allocationType, initialOuterCapacity, innerCapacity);
    }
}
