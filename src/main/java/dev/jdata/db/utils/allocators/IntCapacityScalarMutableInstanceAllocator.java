package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;

public abstract class IntCapacityScalarMutableInstanceAllocator<T extends IMutable, U extends T> extends HeapMutableInstanceAllocator<T> {

    protected abstract U allocateMutable(int minimumCapacity);

    @Override
    public final T createMutable(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return allocateMutable(Capacity.intCapacityRenamed(minimumCapacity));
    }
}
