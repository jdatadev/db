package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeSinglyLinkedNodeList extends BaseMutableLongLargeSinglyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
