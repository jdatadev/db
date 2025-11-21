package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongIndexList extends IMutableLongIndexList, IHeapContainsMarker {

    public static IHeapMutableLongIndexList create(int initialCapacity) {

        return HeapMutableLongIndexList.create(AllocationType.HEAP, initialCapacity);
    }
}
