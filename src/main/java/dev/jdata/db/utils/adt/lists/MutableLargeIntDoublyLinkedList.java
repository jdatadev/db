package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

public final class MutableLargeIntDoublyLinkedList

        extends BaseLargeIntDoublyLinkedList<MutableLargeIntDoublyLinkedList, MutableLargeIntDoublyLinkedList>
        implements IMutableLargeIntList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public MutableLargeIntDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);

        this.headNode = NO_NODE;
        this.tailNode = NO_NODE;

        this.numElements = 0L;
    }

    @Override
    public boolean isEmpty() {

        return isEmpty(headNode, tailNode);
    }

    @Override
    public long getNumElements() {
        return numElements;
    }

    @Override
    public boolean contains(int value) {

        return containsValue(value, headNode);
    }

    @Override
    public boolean containsOnly(int value) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public boolean containsOnly(int value, IContainsOnlyPredicate predicate) {

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public int[] toArray() {

        return toListArrayValues(headNode, intNumElements(numElements));
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        for (long n = headNode; n != NO_NODE; n = getNextNode(n)) {

            forEach.each(getValue(n), parameter);
        }
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        for (long n = headNode; n != NO_NODE; n = getNextNode(n)) {

            final R forEachResult = forEach.each(getValue(n), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    public long getHeadNode() {

        return checkHeadNode(headNode);
    }

    @Override
    public long getTailNode() {

        return checkTailNode(tailNode);
    }

    @Override
    public long addHead(int value) {

        final long result = addHeadValue(this, value, headNode, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(int value) {

        final long result = addTailValue(this, value, headNode, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public int removeHead() {

        final int result = removeHead(this, headNode, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    private long removeHeadNode() {

        final long result = removeHeadNodeAndReturnNode(this, headNode, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeTail() {

        return getValue(removeTailNodeAndReturnNode());
    }

    long removeTailNodeAndReturnNode() {

        final long result = removeTailNodeAndReturnNode(this, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeNode(long node) {

        final long result = getValue(removeListNodeAndReturnNode(this, node, headNode, tailNode, MutableLargeIntDoublyLinkedList::setHeadNode, MutableLargeIntDoublyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    @Override
    final void clearNumElements() {

        this.headNode = NO_NODE;
        this.tailNode = NO_NODE;

        this.numElements = 0L;
    }

    private void setHeadNode(long headNode) {

        this.headNode = headNode;
    }

    private void setTailNode(long tailNode) {

        this.tailNode = tailNode;
    }

    private void increaseNumElements() {

        ++ numElements;
    }

    private void decreaseNumElements() {

        -- numElements;
    }
}
