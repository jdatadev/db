package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongArray extends ILongArray, IHeapArrayMarker {

    @SafeVarargs
    public static IHeapLongArray of(long ... values) {

        return HeapLongArray.of(AllocationType.HEAP, values);
    }

    public static IHeapLongArray copyOf(ILongArrayView toCopy) {

        return HeapLongArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
