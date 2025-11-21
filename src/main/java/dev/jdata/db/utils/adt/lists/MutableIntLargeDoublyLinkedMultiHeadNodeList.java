package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE, IntInnerOuterNodeValues>
        implements IMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE> {

    MutableIntLargeDoublyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
