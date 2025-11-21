package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE, LongInnerOuterNodeListValues> {

    MutableLongLargeDoublyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
