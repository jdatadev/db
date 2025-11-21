package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IView;

public interface INodeListView extends IView, INodeListIterable {

    INodeListView createEmptyWithSameCapacity();

    INodeListView createEmptyWithCapacityExponentIncrease(int capacityExponentIncrease);
}
