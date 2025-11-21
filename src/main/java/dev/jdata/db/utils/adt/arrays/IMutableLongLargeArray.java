package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongLargeArray extends IBaseMutableLongArray, IMutableLargeArrayMarker {

    public static IMutableLongLargeArray create() {

        return Capacity.instantiateOuterCapacityInnerExponent(MutableLongLargeArray::new);
    }

    public static IMutableLongLargeArray create(long initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return Capacity.instantiateOuterCapacityInnerExponent(initialCapacity, MutableLongLargeArray::new);
    }

    public static IMutableLongLargeArray create(int initialOuterCapacity, int innerCapacityExponent, long clearValue) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isInnerCapacityExponent(innerCapacityExponent);

        return new MutableLongLargeArray(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IMutableLongLargeArray copyOf(ILongIterableElementsView toCopy) {

        Objects.requireNonNull(toCopy);

        return MutableLongLargeArray.copyOf((MutableLongLargeArray)toCopy);
    }
}
