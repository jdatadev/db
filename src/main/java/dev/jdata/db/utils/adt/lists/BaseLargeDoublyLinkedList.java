package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseLargeDoublyLinkedList<
                INSTANCE,
                LIST_T,
                LIST extends BaseLargeDoublyLinkedList<INSTANCE, LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>>

        extends BaseLargeList<LIST_T, LIST, VALUES> {

    private long[][] previous;

    BaseLargeDoublyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<LIST_T, LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);

        Checks.isCapacity(initialOuterCapacity);
        Checks.isCapacity(innerCapacity);

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

    final long addHeadNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

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

    final long addTailNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

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

    final long removeHeadNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(instance, newHeadNode);

        addNodeToFreeList(headNode);

        if (newHeadNode == NO_NODE) {

            tailNodeSetter.setNode(instance, NO_NODE);
        }
        else {
            setNode(previous, newHeadNode, NO_NODE);
        }

        return headNode;
    }

    final long removeTailNodeAndReturnNode(INSTANCE instance, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

        final long newTailNode = getPreviousNode(tailNode);

        setNode(previous, tailNode, NO_NODE);

        addNodeToFreeList(tailNode);

        if (newTailNode != NO_NODE) {

            setNextNode(newTailNode, NO_NODE);

            tailNodeSetter.setNode(instance, newTailNode);
        }
        else {
            headNodeSetter.setNode(instance, NO_NODE);
            tailNodeSetter.setNode(instance, NO_NODE);
        }

        return tailNode;
    }

    final long removeListNodeAndReturnNode(INSTANCE instance, long node, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter,
            LongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        final long result;

        if (node == headNode) {

            result = removeHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);
        }
        else if (node == tailNode) {

            result = removeTailNodeAndReturnNode(instance, tailNode, headNodeSetter, tailNodeSetter);
        }
        else {
            final long previousNode = getPreviousNode(node);
            final long nextNode = getNextNode(node);

            if (previousNode != NO_NODE) {

                setNextNode(previousNode, nextNode);

                if (nextNode != NO_NODE) {

                    setNode(previous, nextNode, previousNode);
                }
            }

            result = node;

            addNodeToFreeList(node);
        }

        return result;
    }
}
