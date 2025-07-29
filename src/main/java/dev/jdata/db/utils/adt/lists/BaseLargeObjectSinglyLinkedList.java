package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectElements.IContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElements.IObjectElementPredicate;

abstract class BaseLargeObjectSinglyLinkedList<

                INSTANCE,
                T,
                LIST extends BaseLargeObjectSinglyLinkedList<INSTANCE, T, LIST, VALUES>, VALUES extends BaseObjectValues<T, LIST, VALUES>>

        extends BaseLargeSinglyLinkedList<INSTANCE, T[], LIST, VALUES> {

    BaseLargeObjectSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<T[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

    }

    public final T getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(T value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final <P> boolean containsValue(long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return getValues().containsValue(this, headNode, parameter, predicate);
    }

    final boolean containsOnlyValue(T value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(T value, long headNode, IContainsOnlyPredicate<T> containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findValueNode(T value, long headNode) {

        return getValues().findValueNode(this, value, headNode);
    }

    final <P> T findNodeValue(T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return getValues().findNodeValue(this, defaultValue, headNode, parameter, predicate);
    }

    final long addHeadValue(INSTANCE instance, T value, long headNode, long tailNode,
            ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, T value, long headNode, long tailNode,
            ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final T removeHeadAndReturnValue(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long removedHeadNode = removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return getValue(removedHeadNode);
    }

    private void setValue(long node, T value) {

        getValues().setValue(this, node, value);
    }
}
