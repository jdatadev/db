package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeSinglyLinkedNodeList extends BaseMutableIntLargeSinglyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeSinglyLinkedNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
