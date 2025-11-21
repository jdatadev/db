package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.elements.IByteIterableElementsView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableByteLargeArray extends IMutableByteLargeArray, IHeapArrayMarker {

    public static IHeapMutableByteLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, HeapMutableByteLargeArray::new);
    }

    public static IHeapMutableByteLargeArray create(long initialCapacity) {

        Checks.isLongInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(AllocationType.HEAP, initialCapacity, HeapMutableByteLargeArray::new);
    }

    public static IHeapMutableByteLargeArray create(int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableByteLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableByteLargeArray copyOf(IByteIterableElementsView toCopy) {

        Objects.requireNonNull(toCopy);

        return new HeapMutableByteLargeArray(AllocationType.HEAP, (MutableByteLargeArray)toCopy);
    }
}
