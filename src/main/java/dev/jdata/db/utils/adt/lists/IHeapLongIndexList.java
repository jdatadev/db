package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongIndexList extends ILongIndexList, IHeapContainsMarker {

    public static IHeapLongIndexList of(long ... values) {

        return values.length != 0 ? HeapLongIndexList.of(AllocationType.HEAP, values) : HeapLongIndexList.empty();
    }
}
