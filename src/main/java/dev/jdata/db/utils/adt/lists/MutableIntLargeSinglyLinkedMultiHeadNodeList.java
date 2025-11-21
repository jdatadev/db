package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.checks.Checks;

final class MutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE, IntInnerOuterNodeValues> {

    MutableIntLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, IntInnerOuterNodeValues::new);
    }

    @Override
    public ISinglyLinkedMultiHeadNodeListView createEmptyWithSameCapacity() {

        return new MutableIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE>(geto, getNumOuterAllocatedEntries());
    }

    @Override
    public ISinglyLinkedMultiHeadNodeListView createEmptyWithCapacityExponentIncrease(int capacityExponentIncrease) {

        Checks.isLongCapacityExponentIncrease(capacityExponentIncrease);

        throw new UnsupportedOperationException();
    }
}
