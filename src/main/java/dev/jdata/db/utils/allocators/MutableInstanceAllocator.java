package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableInstanceAllocator<T extends IMutable, U extends Allocatable> extends TrackingInstanceAllocator<T> implements IMutableAllocator<T> {

    protected abstract U allocateMutableInstance(long minimumCapacity);
    public abstract void freeMutableInstance(U mutable);

    @Override
    public final T createMutable(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        @SuppressWarnings("unchecked")
        final T result = (T)allocateMutableInstance(Capacity.intCapacityRenamed(minimumCapacity));

        return result;
    }

    @Override
    public final void freeMutable(T mutable) {

        Objects.requireNonNull(mutable);

        @SuppressWarnings("unchecked")
        final U mutableElements = (U)mutable;

        freeMutableInstance(mutableElements);
    }
}
