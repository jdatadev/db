package dev.jdata.db.utils.adt.lists;

public final class LongDoublyLinkedList extends BaseLongDoublyLinkedList<LongDoublyLinkedList> implements LongList {

    private long head;
    private long tail;

    public LongDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
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

        return addHead(value, head, tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    @Override
    public long addTail(long value) {

        return addTail(value, value, value, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
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

        return removeHead(head, tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    long removeHeadNode() {

        return removeHeadNode(head, tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    public long removeTail() {

        return removeTail(tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    long removeTailNode() {

        return removeTailNode(tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    public long removeNode(long node) {

        return removeNode(node, head, tail, LongDoublyLinkedList::setHead, LongDoublyLinkedList::setTail);
    }

    private void setHead(long head) {

        this.head = head;
    }

    private void setTail(long tail) {

        this.tail = tail;
    }
}
