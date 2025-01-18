package dev.jdata.db.utils.adt.lists;

@Deprecated // currently not in use
abstract class BaseLargeIntSinglyLinkedList<T> extends BaseLargeSinglyLinkedList<T, int[], IntValues> {

    BaseLargeIntSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntValues::new);
    }

    final boolean containsValue(int value, int headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final long findValue(int value, long headNode) {

        return getValues().findValue(this, value, headNode);
    }
}
