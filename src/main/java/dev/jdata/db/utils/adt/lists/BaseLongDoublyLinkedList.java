package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongDoublyLinkedList<T> extends BaseLongList {

    private long[][] previous;

    BaseLongDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);

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

    final long addHeadValue(long value, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long node = allocateNode(next);

        final long headNode = head;

        if (headNode != NO_NODE) {

            setNode(previous, headNode, node);
        }

        setNode(next, node, headNode);
        setNode(previous, node, NO_NODE);

        final T listThis = getThis();

        headSetter.setNode(listThis, node);

        if (tail == NO_NODE) {

            tailSetter.setNode(listThis, node);
        }

        setValue(node, value);

        increaseNumElements();

        return node;
    }

    final long addTail(long value, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long node = allocateNode(next);

        setNode(previous, node, tail);

        if (tail != NO_NODE) {

            setNode(next, tail, node);
        }

        final T listThis = getThis();

        tailSetter.setNode(listThis, node);

        if (head == NO_NODE) {

            setNode(next, node, head);

            headSetter.setNode(listThis, node);
        }

        setNode(next, node, NO_NODE);

        setValue(node, value);

        increaseNumElements();

        return node;
    }

    final long removeHead(long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long headNode = removeHeadNode(head, tail, tailSetter, tailSetter);

        return getValue(headNode);
    }

    final long removeHeadNode(long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (isEmpty(head, tail)) {

            throw new IllegalStateException();
        }

        final long headNode = head;

        final long newHeadNode = getNode(next, headNode);

        final T listThis = getThis();

        headSetter.setNode(listThis, newHeadNode);

        addNodeToFreeList(next, headNode);

        if (newHeadNode == NO_NODE) {

            tailSetter.setNode(listThis, NO_NODE);
        }
        else {
            setNode(previous, newHeadNode, NO_NODE);
        }

        decreaseNumElements();

        return headNode;
    }

    final long removeTail(long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        return getValue(removeTailNode(tail, headSetter, tailSetter));
    }

    final long removeTailNode(long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (tail == NO_NODE) {

            throw new IllegalStateException();
        }

        final long tailNode = tail;

        final long newTailNode = getPreviousNode(tailNode);

        setNode(previous, tailNode, NO_NODE);

        addNodeToFreeList(next, tailNode);

        final T listThis = getThis();

        if (newTailNode != NO_NODE) {

            setNode(next, newTailNode, NO_NODE);

            tailSetter.setNode(listThis, newTailNode);
        }
        else {
            headSetter.setNode(listThis, NO_NODE);
            tailSetter.setNode(listThis, NO_NODE);
        }

        decreaseNumElements();

        return tailNode;
    }

    final long removeNode(long node, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (isEmpty(head, tail)) {

            throw new IllegalStateException();
        }

        final long result;

        if (node == head) {

            result = removeHead(head, tail, headSetter, tailSetter);
        }
        else if (node == tail) {

            result = removeTail(tail, headSetter, tailSetter);
        }
        else {
            final long previousNode = getPreviousNode(node);
            final long nextNode = getNextNode(node);

            if (previousNode != NO_NODE) {

                setNode(next, previousNode, nextNode);

                if (nextNode != NO_NODE) {

                    setNode(previous, nextNode, previousNode);
                }
            }

            result = getValue(node);

            addNodeToFreeList(next, node);

            decreaseNumElements();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private T getThis() {

        return (T)this;
    }
}
