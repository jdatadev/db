package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableIntLargeArray extends IBaseMutableIntArray, IMutableLargeArrayMarker {

    public static IMutableIntLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(MutableIntLargeArray::new);
    }

    public static IMutableIntLargeArray create(long initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(initialCapacity, MutableIntLargeArray::new);
    }

    public static IMutableIntLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacityExponent(innerCapacityExponent);

        return new MutableIntLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    public static IMutableIntLargeArray create(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacityExponent(innerCapacityExponent);

        return new MutableIntLargeArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IMutableIntLargeArray copyOf(IMutableIntLargeArray toCopy) {

        Objects.requireNonNull(toCopy);

        return MutableIntLargeArray.copyOf((MutableIntLargeArray)toCopy);
    }
}
