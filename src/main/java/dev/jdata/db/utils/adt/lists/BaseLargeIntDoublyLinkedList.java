package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.IntList.ContainsOnlyPredicate;

abstract class BaseLargeIntDoublyLinkedList<T> extends BaseLargeDoublyLinkedList<T, int[], IntValues> {

    BaseLargeIntDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntValues::new);
    }

    public final int getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(int value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final boolean containsOnlyValue(int value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(int value, long headNode, ContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValue(int value, long headNode) {

        return getValues().findValue(this, value, headNode);
    }

    final long addHeadValue(T instance, int value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(T instance, int value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final int removeHead(T instance, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    final int removeListNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    private void setValue(long node, int value) {

        getValues().setValue(this, node, value);
    }
}
