package dev.jdata.db.utils.adt.lists;

public final class LargeIntMultiHeadDoublyLinkedList<T> extends BaseLargeIntDoublyLinkedList<T> implements IntMultiList {

    public LargeIntMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    public boolean contains(int value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    public long findNode(int value, long headNode) {

        return getValues().findValue(this, value, headNode);
    }

    public long addHead(T instance, int value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public long addTail(T instance, int value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public int removeNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    @Override
    public int[] toArray(long headNode) {

        return toListArrayValues(headNode);
    }
}
