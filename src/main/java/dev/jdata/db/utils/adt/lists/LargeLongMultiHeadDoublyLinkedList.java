package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.IContainsOnlyPredicate;

public final class LargeLongMultiHeadDoublyLinkedList<INSTANCE>

        extends BaseLargeLongDoublyLinkedList<INSTANCE, LargeLongMultiHeadDoublyLinkedList<INSTANCE>>
        implements ILongMutableMultiList<INSTANCE> {

    public LargeLongMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    public boolean contains(long value, long headNode) {

        return containsValue(value, headNode);
    }

    public boolean containsOnly(long value, long headNode) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public boolean containsOnly(long value, long headNode, IContainsOnlyPredicate predicate) {

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public long findNode(long value, long headNode) {

        return findValue(value, headNode);
    }

    public long addHead(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public long addTail(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    private long removeTail(INSTANCE instance, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return getValue(removeTailNodeAndReturnNode(instance, tailNode, headNodeSetter, tailNodeSetter));
    }

    @Override
    public long removeNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    @Override
    public long[] toArray(long headNode) {

        return toListArrayValues(headNode);
    }

    @Override
    void clearNumElements() {

    }

    public void clear(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        clearNodes(instance, headNode, headNodeSetter, tailNodeSetter);
    }
}
