package dev.jdata.db.utils.adt.lists;

public final class LargeIntDoublyLinkedList extends BaseLargeIntDoublyLinkedList<LargeIntDoublyLinkedList> implements LargeIntList {

    private long headNode;
    private long tailNode;

    private long numElements;

    public LargeIntDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
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
    public boolean containsOnly(int value, ContainsOnlyPredicate predicate) {

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public int[] toArray() {

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
    public long addHead(int value) {

        final long result = addHeadValue(this, value, headNode, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public long addTail(int value) {

        final long result = addTailValue(this, value, headNode, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public int removeHead() {

        final int result = removeHead(this, headNode, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    private long removeHeadNode() {

        final long result = removeHeadNodeAndReturnNode(this, headNode, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeTail() {

        return getValue(removeTailNodeAndReturnNode());
    }

    long removeTailNodeAndReturnNode() {

        final long result = removeTailNodeAndReturnNode(this, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode);

        decreaseNumElements();

        return result;
    }

    public long removeNode(long node) {

        final long result = getValue(removeListNodeAndReturnNode(this, node, headNode, tailNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
    }

    @Override
    public void clear() {

        clearNodes(this, headNode, LargeIntDoublyLinkedList::setHeadNode, LargeIntDoublyLinkedList::setTailNode, l -> l.numElements = 0L);
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
