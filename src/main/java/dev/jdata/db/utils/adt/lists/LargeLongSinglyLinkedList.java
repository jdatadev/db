package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

public final class LargeLongSinglyLinkedList

        extends BaseLargeLongSinglyLinkedList<LargeLongSinglyLinkedList, LargeLongSinglyLinkedList, LongValues<LargeLongSinglyLinkedList>>
        implements IMutableLargeLongList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public LargeLongSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongValues::new);

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
    public long[] toArray() {

        return toListArrayValues(headNode, intNumElements(numElements));
    }

    @Override
    public boolean contains(long value) {

        return getValues().containsValue(this, value, headNode);
    }

    @Override
    public boolean containsOnly(long value) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    @Override
    public boolean containsOnly(long value, IContainsOnlyPredicate predicate) {

        return getValues().containsOnlyValue(this, value, headNode, predicate);
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

        final long result = addHeadValue(this, value, headNode, tailNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(long value) {

        final long result = addTailValue(this, value, headNode, tailNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long removeHead() {

        final long result = removeHeadAndReturnValue(this, headNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    long removeHeadNode() {

        final long result = getValue(removeHeadNodeAndReturnNode(this, headNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    long removeTailNodeAndReturnNode(long newTailNode) {

        final long result = removeTailNodeAndReturnNode(this, newTailNode, tailNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    void removeNode(long node, long previousNode) {

        removeNode(this, node, previousNode, headNode, tailNode, LargeLongSinglyLinkedList::setHeadNode, LargeLongSinglyLinkedList::setTailNode);

        decreaseNumElements();
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
