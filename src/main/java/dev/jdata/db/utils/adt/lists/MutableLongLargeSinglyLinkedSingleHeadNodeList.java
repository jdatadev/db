package dev.jdata.db.utils.adt.lists;

abstract class MutableLongLargeSinglyLinkedSingleHeadNodeList extends BaseMutableLongLargeSinglyLinkedSingleHeadNodeList<LongInnerOuterNodeListValues> {

    MutableLongLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
