package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapLongArray extends ILongArray, IHeapArrayMarker {

    @SafeVarargs
    public static IHeapLongArray of(long ... values) {

        Checks.isNotEmpty(values);

        return HeapLongArray.of(AllocationType.HEAP, values);
    }

    public static IHeapLongArray copyOf(ILongArrayView toCopy) {

        Objects.requireNonNull(toCopy);

        return HeapLongArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
