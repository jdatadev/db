package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableCharLargeArray extends IMutableCharLargeArray, IHeapArrayMarker {

    public static IHeapMutableCharLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, HeapMutableCharLargeArray::new);
    }

    public static IHeapMutableCharLargeArray create(long initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, initialCapacity, HeapMutableCharLargeArray::new);
    }

    public static IHeapMutableCharLargeArray create(int initialOuterCapacity, int innerCapacityExponent, char clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableCharLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableCharLargeArray copyOf(IMutableCharLargeArray toCopy) {

        Objects.requireNonNull(toCopy);

        return new HeapMutableCharLargeArray(AllocationType.HEAP, (MutableCharLargeArray)toCopy);
    }
}
