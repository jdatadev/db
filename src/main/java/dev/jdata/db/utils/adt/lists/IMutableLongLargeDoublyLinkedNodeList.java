package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongLargeDoublyLinkedNodeList extends IBaseMutableLongDoublyLinkedNodeList {

    public static IMutableLongLargeDoublyLinkedNodeList create(int initialOuterCapacity, int innerCapacity) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacity(innerCapacity);

        return new MutableLongLargeDoublyLinkedNodeList(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }
}
