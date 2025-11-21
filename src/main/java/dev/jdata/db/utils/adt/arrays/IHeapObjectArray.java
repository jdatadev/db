package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapObjectArray<T> extends IObjectArray<T>, IHeapArrayMarker {

    @SafeVarargs
    public static <T> IHeapObjectArray<T> of(T ... instances) {

        Checks.isNotEmpty(instances);

        return HeapObjectArray.of(AllocationType.HEAP, instances);
    }

    public static <T> IHeapObjectArray<T> copyOf(IObjectArrayView<T> toCopy) {

        Objects.requireNonNull(toCopy);

        return HeapObjectArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
