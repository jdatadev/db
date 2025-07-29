package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

public final class LargeLongDoublyLinkedList extends BaseLargeLongDoublyLinkedList<LargeLongDoublyLinkedList, LargeLongDoublyLinkedList> implements IMutableLargeLongList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public LargeLongDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
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
    public boolean contains(long value) {

        return containsValue(value, headNode);
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        for (long node = headNode; node != NO_NODE; node = getNextNode(node)) {

            forEach.each(getValue(node), parameter);
        }
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        for (long node = headNode; node != NO_NODE; node = getNextNode(node)) {

            final R forEachResult = forEach.each(getValue(node), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean containsOnly(long value) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public boolean containsOnly(long value, IContainsOnlyPredicate predicate) {

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public long[] toArray() {

        return toListArrayValues(headNode, intNumElements(numElements));
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
    public long addHead(long value) {

        final long result = addHeadValue(this, value, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(long value) {

        final long result = addTailValue(this, value, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long removeHead() {

        final long result = removeHead(this, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    private long removeHeadNodeAndReturNode() {

        final long result = removeHeadNodeAndReturnNode(this, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeTail() {

        return getValue(removeTailNodeAndReturnNode());
    }

    long removeTailNodeAndReturnNode() {

        final long result = removeTailNodeAndReturnNode(this, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeNode(long toRemove) {

        final long result = getValue(removeListNodeAndReturnNode(this, toRemove, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    @Override
    void clearNumElements() {

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
