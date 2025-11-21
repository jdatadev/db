package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongIndexList extends ILongIndexList, IHeapContainsMarker {

    public static IHeapLongIndexList of(long ... values) {

        Objects.requireNonNull(values);

        return HeapLongIndexList.withArray(AllocationType.HEAP, Array.copyOf(values), values.length);
    }
}
