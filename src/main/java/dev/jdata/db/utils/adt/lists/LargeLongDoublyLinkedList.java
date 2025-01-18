package dev.jdata.db.utils.adt.lists;

public final class LargeLongDoublyLinkedList extends BaseLargeLongDoublyLinkedList<LargeLongDoublyLinkedList> implements LargeLongList {

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
    public boolean containsOnly(long value) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public boolean containsOnly(long value, ContainsOnlyPredicate predicate) {

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

    private long removeHeadNode() {

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

    public long removeNode(long node) {

        final long result = getValue(removeListNodeAndReturnNode(this, node, headNode, tailNode, LargeLongDoublyLinkedList::setHeadNode, LargeLongDoublyLinkedList::setTailNode));

        decreaseNumElements();

        return result;
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
