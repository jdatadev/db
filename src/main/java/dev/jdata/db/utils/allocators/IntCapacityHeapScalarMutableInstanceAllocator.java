package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;
import dev.jdata.db.utils.adt.mutability.IMutable;

public abstract class IntCapacityHeapScalarMutableInstanceAllocator<T extends IMutable & IHeapTypeMarker, U extends T, V extends IMutableFrom>

        extends IntCapacityHeapMutableInstanceAllocator<T, V> {

    protected abstract U allocateMutable(int minimumCapacity);

    @Override
    public final T createMutable(long minimumCapacity) {

        checkCreateMutableParameters(CAPACITY_MAX, minimumCapacity);

        return allocateMutable(Capacity.intCapacityRenamed(minimumCapacity));
    }
}
