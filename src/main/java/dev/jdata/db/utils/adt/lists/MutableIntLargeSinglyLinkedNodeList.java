package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeSinglyLinkedNodeList extends BaseMutableIntLargeSinglyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
