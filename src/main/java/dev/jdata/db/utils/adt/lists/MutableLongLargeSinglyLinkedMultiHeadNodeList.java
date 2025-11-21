package dev.jdata.db.utils.adt.lists;

abstract class MutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, LongInnerOuterNodeListValues> {

    MutableLongLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
