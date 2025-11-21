package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongLargeSinglyLinkedNodeList extends IBaseMutableLongSinglyLinkedNodeList {

    public static IMutableLongLargeSinglyLinkedNodeList create(int initialOuterCapacity, int innerCapacity) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacity(innerCapacity);

        return new MutableLongLargeSinglyLinkedNodeList(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
