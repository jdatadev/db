package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeDoublyLinkedNodeList extends BaseMutableIntLargeDoublyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeDoublyLinkedNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
