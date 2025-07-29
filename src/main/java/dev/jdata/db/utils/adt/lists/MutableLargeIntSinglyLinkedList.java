package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

public final class MutableLargeIntSinglyLinkedList

        extends BaseLargeIntSinglyLinkedList<MutableLargeIntSinglyLinkedList, MutableLargeIntSinglyLinkedList, IntValues<MutableLargeIntSinglyLinkedList>>
        implements IMutableLargeIntList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public MutableLargeIntSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntValues::new);

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
    public int[] toArray() {

        return toListArrayValues(headNode, intNumElements(numElements));
    }

    @Override
    public boolean contains(int value) {

        return getValues().containsValue(this, value, headNode);
    }

    @Override
    public boolean containsOnly(int value) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    @Override
    public boolean containsOnly(int value, IContainsOnlyPredicate predicate) {

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
    public long addHead(int value) {

        final long result = addHeadValue(this, value, headNode, tailNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(int value) {

        final long result = addTailValue(this, value, headNode, tailNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public int removeHead() {

        final int result = removeHeadAndReturnValue(this, headNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    long removeHeadNode() {

        final long result = getValue(removeHeadNodeAndReturnNode(this, headNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    long removeTailNodeAndReturnNode(long newTailNode) {

        final long result = removeTailNodeAndReturnNode(this, newTailNode, tailNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    void removeNode(long toRemove, long previousNode) {

        removeNode(this, toRemove, previousNode, headNode, tailNode, MutableLargeIntSinglyLinkedList::setHeadNode, MutableLargeIntSinglyLinkedList::setTailNode);

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
