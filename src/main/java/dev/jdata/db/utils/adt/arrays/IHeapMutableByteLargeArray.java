package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.IByteIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableByteLargeArray extends IMutableByteLargeArray, IHeapArrayMarker {

    public static IHeapMutableByteLargeArray create() {

        return HeapMutableByteLargeArray.create(AllocationType.HEAP);
    }

    public static IHeapMutableByteLargeArray create(long initialCapacity) {

        return HeapMutableByteLargeArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableByteLargeArray create(int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {

        return HeapMutableByteLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableByteLargeArray copyOf(IByteIterableElementsView toCopy) {

        return HeapMutableByteLargeArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
