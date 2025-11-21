package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeSinglyLinkedNodeList extends BaseMutableLongLargeSinglyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeSinglyLinkedNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
