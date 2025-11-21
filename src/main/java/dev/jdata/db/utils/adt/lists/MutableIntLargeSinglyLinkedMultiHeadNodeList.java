package dev.jdata.db.utils.adt.lists;

abstract class MutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE, IntInnerOuterNodeValues> {

    MutableIntLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
