package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectElements.IObjectElementPredicate;

public abstract class BaseLargeObjectMultiHeadSinglyLinkedList<

                INSTANCE,
                T,
                LIST extends BaseLargeObjectSinglyLinkedList<INSTANCE, T, LIST, VALUES>,
                VALUES extends BaseObjectValues<T, LIST, VALUES>>

        extends BaseLargeObjectSinglyLinkedList<INSTANCE, T, LIST, VALUES> {

    protected BaseLargeObjectMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<T[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final boolean contains(T value, long headNode) {

        return containsValue(value, headNode);
    }

    public final <P> boolean contains(long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return containsValue(headNode, parameter, predicate);
    }

    public final <P> long findNodeWithValue(T value, long headNode) {

        return findValueNode(value, headNode);
    }

    public final <P> T findValue(T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return findNodeValue(defaultValue, headNode, parameter, predicate);
    }

    public final long addHead(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long addTail(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final T removeNode(INSTANCE instance, long node, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(node);
    }

    public final long removeAtMostOneNodeByValue(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        long previousNode = noNode;
        long removedNode = noNode;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            if (getValue(node) == value) {

                if (removedNode != noNode) {

                    throw new IllegalStateException();
                }

                removeNode(instance, node, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removedNode = node;
            }

            previousNode = node;
        }

        return removedNode;
    }

    @Override
    final void clearNumElements() {

    }
}
