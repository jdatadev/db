package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongLargeArray extends IMutableLongLargeArray, IHeapArrayMarker {

    public static IHeapMutableLongLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, HeapMutableLongLargeArray::new);
    }

    public static IHeapMutableLongLargeArray create(long initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, initialCapacity, HeapMutableLongLargeArray::new);
    }

    public static IHeapMutableLongLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableLongLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }

    public static IHeapMutableLongLargeArray create(int initialOuterCapacity, int innerCapacityExponent, long clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableLongLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableLongLargeArray copyOf(ILongIterableElementsView toCopy) {

        Objects.requireNonNull(toCopy);

        return new HeapMutableLongLargeArray(AllocationType.HEAP, (MutableLongLargeArray)toCopy);
    }
}
