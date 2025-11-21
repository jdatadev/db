package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeDoublyLinkedNodeList extends BaseMutableIntLargeDoublyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeDoublyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
