package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.IntList.ContainsOnlyPredicate;

abstract class BaseLargeIntSinglyLinkedList<T> extends BaseLargeSinglyLinkedList<T, int[], IntValues> {

    BaseLargeIntSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntValues::new);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

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

    final int removeHeadAndReturnValue(T instance, long headNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    private void setValue(long node, int value) {

        getValues().setValue(this, node, value);
    }
}
