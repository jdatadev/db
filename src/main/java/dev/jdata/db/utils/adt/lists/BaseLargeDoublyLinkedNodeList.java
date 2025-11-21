package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseLargeDoublyLinkedNodeList<

                INSTANCE,
                TO_ARRAY,
                VALUES_LIST extends IInnerOuterNodeListInternal<TO_ARRAY>,
                VALUES extends BaseInnerOuterNodeListValues<TO_ARRAY, VALUES_LIST>>

        extends BaseLargeNodeList<TO_ARRAY, VALUES_LIST, VALUES> {

    private long[][] previous;

    BaseLargeDoublyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<TO_ARRAY, VALUES_LIST, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacity(innerCapacity);

        this.previous = new long[initialOuterCapacity][];

        previous[0] = allocateListNodes(innerCapacity);
    }

    public final long getPreviousNode(long node) {

        return getNode(previous, node);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

        Checks.isLengthAboveZero(newOuterLength);

        this.previous = Arrays.copyOf(previous, newOuterLength);
    }

    @Override
    final void allocateInner(int outerIndex, int innerArrayCapacity) {

        previous[outerIndex] = new long[innerArrayCapacity];
    }

    final long addHeadNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = allocateNextNode();

        if (headNode != NO_NODE) {

            setNode(previous, headNode, node);
        }

        setNextNode(node, headNode);
        setNode(previous, node, NO_NODE);

        headNodeSetter.setNode(instance, node);

        if (tailNode == NO_NODE) {

            tailNodeSetter.setNode(instance, node);
        }

        return node;
    }

    final long addTailNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = allocateNextNode();

        setNode(previous, node, tailNode);

        if (tailNode != NO_NODE) {

            setNextNode(tailNode, node);
        }

        tailNodeSetter.setNode(instance, node);

        if (headNode == NO_NODE) {

            setNextNode(node, headNode);

            headNodeSetter.setNode(instance, node);
        }

        setNextNode(node, NO_NODE);

        return node;
    }

    final void removeHeadNode(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(instance, newHeadNode);

        addNodeToFreeList(headNode);

        final long noNode = NO_NODE;

        if (newHeadNode == noNode) {

            tailNodeSetter.setNode(instance, noNode);
        }
        else {
            setNode(previous, newHeadNode, noNode);
        }
    }

    final void removeTailNode(INSTANCE instance, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        if (tailNode == noNode) {

            throw new IllegalStateException();
        }

        final long newTailNode = getPreviousNode(tailNode);

        setNode(previous, tailNode, noNode);

        addNodeToFreeList(tailNode);

        if (newTailNode != noNode) {

            setNextNode(newTailNode, noNode);

            tailNodeSetter.setNode(instance, newTailNode);
        }
        else {
            headNodeSetter.setNode(instance, noNode);
            tailNodeSetter.setNode(instance, noNode);
        }
    }

    final void removeListNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        if (toRemove == headNode) {

            removeHeadNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);
        }
        else if (toRemove == tailNode) {

            removeTailNode(instance, tailNode, headNodeSetter, tailNodeSetter);
        }
        else {
            final long noNode = NO_NODE;

            final long previousNode = getPreviousNode(toRemove);
            final long nextNode = getNextNode(toRemove);

            if (previousNode != noNode) {

                setNextNode(previousNode, nextNode);

                if (nextNode != noNode) {

                    setNode(previous, nextNode, previousNode);
                }
            }

            addNodeToFreeList(toRemove);
        }
    }
}
