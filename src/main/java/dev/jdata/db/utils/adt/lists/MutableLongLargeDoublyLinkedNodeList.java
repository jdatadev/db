package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeDoublyLinkedNodeList extends BaseMutableLongLargeDoublyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeDoublyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
