package dev.jdata.db.utils.adt.lists;

abstract class BaseLongSinglyLinkedList<T> extends BaseLongList {

    BaseLongSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

    }

    final long addHead(long value, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long node = allocateNode();

        setNextNode(node, head);

        final T thisList = getThis();

        headSetter.setNode(thisList, node);

        if (tail == NO_NODE) {

            tailSetter.setNode(thisList, node);
        }

        setValue(node, value);

        increaseNumElements();

        return node;
    }

    final long addTail(long value, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long node = allocateNode();

        if (tail != NO_NODE) {

            setNextNode(tail, node);
        }

        final T thisList = getThis();

        tailSetter.setNode(thisList, node);

        if (head == NO_NODE) {

            setNextNode(node, head);

            headSetter.setNode(thisList, node);
        }

        setNextNode(node, NO_NODE);

        setValue(node, value);

        increaseNumElements();

        return node;
    }

    final long removeHead(long head, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        final long headNode = removeHeadNode(head, headSetter, tailSetter);

        return getValue(headNode);
    }

    final long removeHeadNode(long head, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (head == NO_NODE) {

            throw new IllegalStateException();
        }

        final long headNode = head;

        final T listThis = getThis();

        headSetter.setNode(listThis, getNextNode(headNode));

        addNodeToFreeList(headNode);

        if (head == NO_NODE) {

            tailSetter.setNode(listThis, NO_NODE);
        }

        decreaseNumElements();

        return headNode;
    }

    final long removeTailNode(long newTailNode, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (tail == NO_NODE) {

            throw new IllegalStateException();
        }

        final long tailNode = tail;

        addNodeToFreeList(tailNode);

        final T listThis = getThis();

        if (newTailNode != NO_NODE) {

            setNextNode(newTailNode, NO_NODE);

            tailSetter.setNode(listThis, newTailNode);
        }
        else {
            headSetter.setNode(listThis, NO_NODE);
            tailSetter.setNode(listThis, NO_NODE);
        }

        decreaseNumElements();

        return tailNode;
    }

    final void removeNode(long node, long previousNode, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        if (isEmpty(head, tail)) {

            throw new IllegalStateException();
        }

        if (node == head) {

            removeHeadNode(head, headSetter, tailSetter);
        }
        else if (node == tail) {

            removeTailNode(previousNode, tail, headSetter, tailSetter);
        }
        else {
            if (previousNode != NO_NODE) {

                final long nextNode = getNextNode(node);

                setNextNode(previousNode, nextNode);
            }

            addNodeToFreeList(node);

            decreaseNumElements();
        }
    }

    @SuppressWarnings("unchecked")
    private T getThis() {

        return (T)this;
    }
}
