package dev.jdata.db.utils.adt.lists;

public interface ISinglyLinkedMultiHeadNodeListView extends IMultiHeadNodeListView {

    ISinglyLinkedMultiHeadNodeListView createEmptyWithSameCapacity();

    ISinglyLinkedMultiHeadNodeListView createEmptyWithCapacityExponentIncrease(int capacityExponentIncrease);
}
