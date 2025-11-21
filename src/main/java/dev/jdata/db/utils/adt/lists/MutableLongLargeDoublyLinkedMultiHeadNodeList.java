package dev.jdata.db.utils.adt.lists;

final class MutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE, LongInnerOuterNodeListValues> {

    MutableLongLargeDoublyLinkedMultiHeadNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }
}
