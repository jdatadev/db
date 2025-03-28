package dev.jdata.db.utils.adt.lists;

public final class LargeLongSinglyLinkedList extends BaseLargeLongSinglyLinkedList<LargeLongSinglyLinkedList> implements LargeLongList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public LargeLongSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
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
    public boolean containsOnly(long value, ContainsOnlyPredicate predicate) {

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
