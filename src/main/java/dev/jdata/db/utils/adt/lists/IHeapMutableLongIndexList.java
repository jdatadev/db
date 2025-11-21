package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongIndexList extends IMutableLongIndexList, IHeapContainsMarker {

    public static IHeapMutableLongIndexList create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableLongIndexList(AllocationType.HEAP, initialCapacity);
    }
}
