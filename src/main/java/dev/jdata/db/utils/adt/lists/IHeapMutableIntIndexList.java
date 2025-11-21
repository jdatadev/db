package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntIndexList extends IMutableIntIndexList, IHeapContainsMarker {

    public static IHeapMutableIntIndexList create(int initialCapacity) {

        return HeapMutableIntIndexList.create(AllocationType.HEAP, initialCapacity);
    }
}
