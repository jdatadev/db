package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.LongList.ContainsOnlyPredicate;

public final class LargeLongMultiHeadDoublyLinkedList<T> extends BaseLargeLongDoublyLinkedList<T> implements LongMultiList {

    public LargeLongMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    public boolean contains(long value, long headNode) {

        return containsValue(value, headNode);
    }

    public boolean containsOnly(long value, long headNode) {

        return containsOnlyValue(value, headNode);
    }

    public boolean containsOnly(long value, long headNode, ContainsOnlyPredicate predicate) {

        return containsOnlyValue(value, headNode, predicate);
    }

    public long findNode(long value, long headNode) {

        return findValue(value, headNode);
    }

    public long addHead(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    private long removeTail(T instance, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return getValue(removeTailNodeAndReturnNode(instance, tailNode, headNodeSetter, tailNodeSetter));
    }

    public long removeNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    @Override
    public long[] toArray(long headNode) {

        return toListArrayValues(headNode);
    }
}
