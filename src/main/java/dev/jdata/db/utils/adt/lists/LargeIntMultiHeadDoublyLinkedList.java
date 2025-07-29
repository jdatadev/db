package dev.jdata.db.utils.adt.lists;

public final class LargeIntMultiHeadDoublyLinkedList<INSTANCE>

        extends BaseLargeIntDoublyLinkedList<INSTANCE, LargeIntMultiHeadDoublyLinkedList<INSTANCE>>
        implements IIntMultiList {

    public LargeIntMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    public boolean contains(int value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    public long findNode(int value, long headNode) {

        return getValues().findValueNode(this, value, headNode);
    }

    public long addHead(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public long addTail(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public int removeNode(INSTANCE instance, long node, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    @Override
    public int[] toArray(long headNode) {

        return toListArrayValues(headNode);
    }

    @Override
    void clearNumElements() {

    }
}
