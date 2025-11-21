package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableCharLargeArray extends IBaseMutableCharArray, IMutableLargeArrayMarker {

    public static IMutableCharLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(MutableCharLargeArray::new);
    }

    public static IMutableCharLargeArray create(long initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(initialCapacity, MutableCharLargeArray::new);
    }

    public static IMutableCharLargeArray create(int initialOuterCapacity, int innerCapacityExponent, char clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacityExponent(innerCapacityExponent);

        return new MutableCharLargeArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IMutableCharLargeArray copyOf(IMutableCharLargeArray toCopy) {

        Objects.requireNonNull(toCopy);

        return new MutableCharLargeArray((MutableCharLargeArray)toCopy);
    }
}
