package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongLargeArray extends IMutableLongLargeArray, IHeapArrayMarker {

    public static IHeapMutableLongLargeArray create() {

        return HeapMutableLongLargeArray.create(AllocationType.HEAP);
    }

    public static IHeapMutableLongLargeArray create(long initialCapacity) {

        return HeapMutableLongLargeArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableLongLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        return HeapMutableLongLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    public static IHeapMutableLongLargeArray create(int initialOuterCapacity, int innerCapacityExponent, long clearValue) {

        return HeapMutableLongLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableLongLargeArray copyOf(ILongIterableElementsView toCopy) {

        return HeapMutableLongLargeArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
