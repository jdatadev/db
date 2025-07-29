package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.ILongElementPredicate;

public abstract class BaseLargeLongMultiHeadSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeLongSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseLongValues<LIST, VALUES>>

        extends BaseLargeLongSinglyLinkedList<INSTANCE, LIST, VALUES> {

    protected BaseLargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<long[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final boolean contains(long value, long headNode) {

        return containsValue(value, headNode);
    }

    public final <P> boolean contains(long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return containsValue(headNode, parameter, predicate);
    }

    public final <P> long findNodeWithValue(long value, long headNode) {

        return findValueNode(value, headNode);
    }

    public final <P> long findValue(long defaultValue, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return findNodeValue(defaultValue, headNode, parameter, predicate);
    }

    public final long addHead(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long addTail(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long removeNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(toRemove);
    }

    public final long removeAtMostOneNodeByValue(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
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
