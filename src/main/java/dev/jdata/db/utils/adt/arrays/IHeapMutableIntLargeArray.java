package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIntLargeArray extends IMutableIntLargeArray, IHeapArrayMarker {

    public static IHeapMutableIntLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, HeapMutableIntLargeArray::new);
    }

    public static IHeapMutableIntLargeArray create(long initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, initialCapacity, HeapMutableIntLargeArray::new);
    }

    public static IHeapMutableIntLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableIntLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    public static IHeapMutableIntLargeArray create(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableIntLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableIntLargeArray copyOf(IMutableIntLargeArray toCopy) {

        Objects.requireNonNull(toCopy);

        return new HeapMutableIntLargeArray(AllocationType.HEAP, (MutableIntLargeArray)toCopy);
    }
}
