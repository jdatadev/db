package dev.jdata.db.utils.adt.lists;

public final class LongSinglyLinkedList extends BaseLongSinglyLinkedList<LongSinglyLinkedList> implements LongList {

    private long head;
    private long tail;

    public LongSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);

        this.head = NO_NODE;
        this.tail = NO_NODE;
    }

    @Override
    public boolean isEmpty() {

        return isEmpty(head, tail);
    }

    @Override
    public long addHead(long value) {

        return addHead(value, head, tail, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    @Override
    public long addTail(long value) {

        return addTail(value, head, tail, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    @Override
    public long getHeadNode() {

        return getHeadNode(head);
    }

    @Override
    public long getTailNode() {

        return getTailNode(tail);
    }

    @Override
    public long removeHead() {

        return removeHead(head, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    long removeHeadNode() {

        return removeHeadNode(head, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    long removeTailNode(long newTailNode) {

        return removeTailNode(newTailNode, tail, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    void removeNode(long node, long previousNode) {

        removeNode(node, previousNode, head, tail, LongSinglyLinkedList::setHead, LongSinglyLinkedList::setTail);
    }

    private void setHead(long head) {

        this.head = head;
    }

    private void setTail(long tail) {

        this.tail = tail;
    }
}
