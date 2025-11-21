package dev.jdata.db.utils.adt.lists;

abstract class MutableLongLargeDoublyLinkedSingleHeadNodeList extends BaseMutableLongLargeDoublyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
