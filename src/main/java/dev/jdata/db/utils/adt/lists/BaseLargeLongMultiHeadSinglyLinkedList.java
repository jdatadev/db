package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.LongElementPredicate;

public abstract class BaseLargeLongMultiHeadSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeLongSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseLongValues<LIST, VALUES>>

        extends BaseLargeLongSinglyLinkedList<INSTANCE, LIST, VALUES> {

    protected BaseLargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<long[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final boolean contains(long value, long headNode) {

        return containsValue(value, headNode);
    }

    public final <P> boolean contains(long headNode, P parameter, LongElementPredicate<P> predicate) {

        return containsValue(headNode, parameter, predicate);
    }

    public final <P> long findNodeWithValue(long value, long headNode) {

        return findValueNode(value, headNode);
    }

    public final <P> long findValue(long defaultValue, long headNode, P parameter, LongElementPredicate<P> predicate) {

        return findNodeValue(defaultValue, headNode, parameter, predicate);
    }

    public final long addHead(INSTANCE instance, long value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long addTail(INSTANCE instance, long value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long removeNode(INSTANCE instance, long node, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(node);
    }

    public final long removeAtMostOneNodeByValue(INSTANCE instance, long value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter,
            LongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        long previousNode = noNode;
        long removedNode = noNode;

        for (long n = headNode; n != noNode; n = getNextNode(n)) {

            if (getValue(n) == value) {

                if (removedNode != noNode) {

                    throw new IllegalStateException();
                }

                removeNode(instance, n, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removedNode = n;
            }

            previousNode = n;
        }

        return removedNode;
    }

    @Override
    final void clearNumElements() {

    }
}
