package dev.jdata.db.utils.adt.lists;

abstract class MutableIntLargeDoublyLinkedSingleHeadNodeList extends BaseMutableIntLargeDoublyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
