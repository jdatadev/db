package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntElements.IContainsOnlyPredicate;

abstract class BaseLargeIntDoublyLinkedList<
                INSTANCE,
                LIST extends BaseLargeIntDoublyLinkedList<INSTANCE, LIST>>

        extends BaseLargeDoublyLinkedList<INSTANCE, int[], LIST, IntValues<LIST>> {

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

    final boolean containsOnlyValue(int value, long headNode, IContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValue(int value, long headNode) {

        return getValues().findValueNode(this, value, headNode);
    }

    final long addHeadValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final int removeHead(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    final int removeListNode(INSTANCE instance, long node, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return getValue(removeListNodeAndReturnNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter));
    }

    private void setValue(long node, int value) {

        getValues().setValue(this, node, value);
    }
}
