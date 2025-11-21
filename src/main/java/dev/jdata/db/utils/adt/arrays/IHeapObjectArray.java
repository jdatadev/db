package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapObjectArray<T> extends IArray<T>, IHeapArrayMarker {

    @SafeVarargs
    public static <T> IHeapObjectArray<T> of(T ... instances) {

        return HeapObjectArray.of(AllocationType.HEAP, instances);
    }

    public static <T> IHeapObjectArray<T> copyOf(IArrayView<T> toCopy) {

        return HeapObjectArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
