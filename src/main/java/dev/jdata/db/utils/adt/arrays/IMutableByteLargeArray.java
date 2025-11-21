package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.elements.IByteIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableByteLargeArray extends IBaseMutableByteArray {
heap
    public static IMutableByteLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(MutableByteLargeArray::new);
    }

    public static IMutableByteLargeArray create(long initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(initialCapacity, MutableByteLargeArray::new);
    }

    public static IMutableByteLargeArray create(int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacityExponent(innerCapacityExponent);

        return new MutableByteLargeArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IMutableByteLargeArray copyOf(IByteIterableElementsView toCopy) {

        Objects.requireNonNull(toCopy);

        return MutableByteLargeArray.copyOf((MutableByteLargeArray)toCopy);
    }
}
