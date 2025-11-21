package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.checks.Checks;

final class MutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE> extends BaseMutableLongLargeSinglyLinkedMultiHeadNodeList<INSTANCE, LongInnerOuterNodeListValues> {

    MutableLongLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
        super(allocationType, initialOuterCapacity, innerCapacity, LongInnerOuterNodeListValues::new);
    }

    @Override
    public ISinglyLinkedMultiHeadNodeListView createEmptyWithSameCapacity() {

        return instantiateOuterCapacityInnerCapacity(MutableLongLargeSinglyLinkedMultiHeadNodeList::new);
    }

    @Override
    public ISinglyLinkedMultiHeadNodeListView createEmptyWithCapacityExponentIncrease(int capacityExponentIncrease) {

        Checks.isLongCapacityExponentIncrease(capacityExponentIncrease);

        throw new UnsupportedOperationException();
    }
}
