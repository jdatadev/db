package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.LongList.ContainsOnlyPredicate;

abstract class BaseLargeLongDoublyLinkedList<T> extends BaseLargeDoublyLinkedList<T, long[], LongValues> {

    BaseLargeLongDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongValues::new);
    }

    public final long getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(long value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final boolean containsOnlyValue(long value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(long value, long headNode, ContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValue(long value, long headNode) {

        return getValues().findValue(this, value, headNode);
    }

    final long addHeadValue(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long removeHead(T instance, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    private void setValue(long node, long value) {

        getValues().setValue(this, node, value);
    }
}
