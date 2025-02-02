package dev.jdata.db.utils.adt.lists;

abstract class BaseLargeSinglyLinkedList<T, U, V extends BaseValues<U, BaseInnerOuterList<U, V>, V>> extends BaseLargeList<U, V> {

    BaseLargeSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<U, V> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void allocateInner(int outerIndex, int innerCapacity) {

    }

    final long addHeadNodeAndReturnNode(long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = allocateNextNode();

        setNextNode(node, headNode);

        final T thisList = getThis();

        headNodeSetter.setNode(thisList, node);

        if (tailNode == NO_NODE) {

            tailNodeSetter.setNode(thisList, node);
        }

        return node;
    }

    final long addTailNodeAndReturnNode(long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long node = allocateNextNode();

        if (tailNode != NO_NODE) {

            setNextNode(tailNode, node);
        }

        final T thisList = getThis();

        tailNodeSetter.setNode(thisList, node);

        if (headNode == NO_NODE) {

            setNextNode(node, headNode);

            headNodeSetter.setNode(thisList, node);
        }

        setNextNode(node, NO_NODE);

        return node;
    }

    final long removeHeadNodeAndReturnNode(long headNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (headNode == NO_NODE) {

            throw new IllegalStateException();
        }

        final T listThis = getThis();

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(listThis, newHeadNode);

        addNodeToFreeList(headNode);

        if (newHeadNode == NO_NODE) {

            tailNodeSetter.setNode(listThis, NO_NODE);
        }

        return headNode;
    }

    final long removeTailNodeAndReturnNode(long newTailNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

        addNodeToFreeList(tailNode);

        final T listThis = getThis();

        if (newTailNode != NO_NODE) {

            setNextNode(newTailNode, NO_NODE);

            tailNodeSetter.setNode(listThis, newTailNode);
        }
        else {
            headNodeSetter.setNode(listThis, NO_NODE);
            tailNodeSetter.setNode(listThis, NO_NODE);
        }

        return tailNode;
    }

    final void removeNode(long node, long previousNode, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        if (node == headNode) {

            removeHeadNodeAndReturnNode(headNode, headNodeSetter, tailNodeSetter);
        }
        else if (node == tailNode) {

            removeTailNodeAndReturnNode(previousNode, tailNode, headNodeSetter, tailNodeSetter);
        }
        else {
            if (previousNode != NO_NODE) {

                final long nextNode = getNextNode(node);

                setNextNode(previousNode, nextNode);
            }

            addNodeToFreeList(node);
        }
    }

    @SuppressWarnings("unchecked")
    private T getThis() {

        return (T)this;
    }
}
