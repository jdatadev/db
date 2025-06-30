package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntElements.IContainsOnlyPredicate;

abstract class BaseLargeIntSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeIntSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseIntValues<LIST, VALUES>>

        extends BaseLargeSinglyLinkedList<INSTANCE, int[], LIST, VALUES> {

    BaseLargeIntSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<int[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
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

    final boolean containsOnlyValue(int value, long headNode, IContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValueNode(int value, long headNode) {

        return getValues().findValueNode(this, value, headNode);
    }

    final long addHeadValue(INSTANCE instance, int value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, int value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final int removeHeadAndReturnValue(INSTANCE instance, long headNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    private void setValue(long node, int value) {

        getValues().setValue(this, node, value);
    }
}
