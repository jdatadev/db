package dev.jdata.db.utils.adt.lists;

public final class LargeIntSinglyLinkedList extends BaseLargeIntSinglyLinkedList<LargeIntSinglyLinkedList> implements LargeIntList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public LargeIntSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
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
    public boolean containsOnly(int value, ContainsOnlyPredicate predicate) {

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

        final long result = addHeadValue(this, value, headNode, tailNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(int value) {

        final long result = addTailValue(this, value, headNode, tailNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public int removeHead() {

        final int result = removeHeadAndReturnValue(this, headNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    long removeHeadNode() {

        final long result = getValue(removeHeadNodeAndReturnNode(this, headNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    long removeTailNodeAndReturnNode(long newTailNode) {

        final long result = removeTailNodeAndReturnNode(this, newTailNode, tailNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    void removeNode(long node, long previousNode) {

        removeNode(this, node, previousNode, headNode, tailNode, LargeIntSinglyLinkedList::setHeadNode, LargeIntSinglyLinkedList::setTailNode);

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
