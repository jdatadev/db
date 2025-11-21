package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeDoublyLinkedNodeList extends BaseMutableLongLargeDoublyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeDoublyLinkedNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
