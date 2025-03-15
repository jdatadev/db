package dev.jdata.db.utils.adt.lists;

abstract class BaseLargeSinglyLinkedList<T, U, V extends BaseValues<U, BaseInnerOuterList<U, V>, V>> extends BaseLargeList<U, V> {

    BaseLargeSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<U, V> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void allocateInner(int outerIndex, int innerCapacity) {

    }

    final long addHeadNodeAndReturnNode(T instance, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = allocateNextNode();

        setNextNode(node, headNode);

        headNodeSetter.setNode(instance, node);

        if (tailNode == NO_NODE) {

            tailNodeSetter.setNode(instance, node);
        }

        return node;
    }

    final long addTailNodeAndReturnNode(T instance, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = allocateNextNode();

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

    final long removeHeadNodeAndReturnNode(T instance, long headNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (headNode == NO_NODE) {

            throw new IllegalStateException();
        }

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(instance, newHeadNode);

        addNodeToFreeList(headNode);

        if (newHeadNode == NO_NODE) {

            tailNodeSetter.setNode(instance, NO_NODE);
        }

        return headNode;
    }

    final long removeTailNodeAndReturnNode(T instance, long newTailNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

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

    final void removeNodeByFindingPreviousNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        long previousNode = NO_NODE;

        for (long n = headNode; n != node; n = getNextNode(n)) {

            previousNode = n;
        }

        removeNode(instance, node, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    final void removeNode(T instance, long node, long previousNode, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        if (node == headNode) {

            removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);
        }
        else if (node == tailNode) {

            removeTailNodeAndReturnNode(instance, previousNode, tailNode, headNodeSetter, tailNodeSetter);
        }
        else {
            if (previousNode != NO_NODE) {

                final long nextNode = getNextNode(node);

                setNextNode(previousNode, nextNode);
            }

            addNodeToFreeList(node);
        }
    }
}
