package dev.jdata.db.utils.adt.lists;

public final class LongMultiHeadDoublyLinkedList<T> extends BaseLongDoublyLinkedList<T> {

    public LongMultiHeadDoublyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    @Override
    public boolean isEmpty() {

        throw new UnsupportedOperationException();
    }

    public boolean contains(long value, long head) {

        return containsValue(value, head);
    }

    public long addHead(long value, long head, long tail, LongNodeSetter<T> headSetter, LongNodeSetter<T> tailSetter) {

        return addHeadValue(value, head, tail, headSetter, tailSetter);
    }
}
