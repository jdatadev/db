package dev.jdata.db.utils.adt.lists;

final class MutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE, IntInnerOuterNodeValues>
        implements IMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE> {

    MutableIntLargeDoublyLinkedMultiHeadNodeList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }
}
