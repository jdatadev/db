package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.IContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElements.LongElementPredicate;

abstract class BaseLargeLongSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeLongSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseLongValues<LIST, VALUES>>

        extends BaseLargeSinglyLinkedList<INSTANCE, long[], LIST, VALUES> {

    BaseLargeLongSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

    }

    public final long getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(long value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final <P> boolean containsValue(long headNode, P parameter, LongElementPredicate<P> predicate) {

        return getValues().containsValue(this, headNode, parameter, predicate);
    }

    final boolean containsOnlyValue(long value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(long value, long headNode, IContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValueNode(long value, long headNode) {

        return getValues().findValueNode(this, value, headNode);
    }

    final <P> long findNodeValue(long defaultValue, long headNode, P parameter, LongElementPredicate<P> predicate) {

        return getValues().findNodeValue(this, defaultValue, headNode, parameter, predicate);
    }

    final long addHeadValue(INSTANCE instance, long value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, long value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long removeHeadAndReturnValue(INSTANCE instance, long headNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    private void setValue(long node, long value) {

        getValues().setValue(this, node, value);
    }
}
