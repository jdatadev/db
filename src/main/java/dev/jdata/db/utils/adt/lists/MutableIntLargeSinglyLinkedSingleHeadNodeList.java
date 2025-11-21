package dev.jdata.db.utils.adt.lists;

abstract class MutableIntLargeSinglyLinkedSingleHeadNodeList extends BaseMutableIntLargeSinglyLinkedSingleHeadNodeList<IntInnerOuterNodeValues> {

    MutableIntLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
