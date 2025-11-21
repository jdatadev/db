package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

abstract class MutableInstanceAllocator<T extends IMutable, U extends Allocatable, V extends IMutableFrom>

        extends TrackingInstanceAllocator<T>
        implements IMutableAllocator<T, V> {

    protected static void checkCopyToMutableParameters(IMutableFrom mutableFrom) {

        Objects.requireNonNull(mutableFrom);
    }

    protected abstract U allocateMutableInstance(long minimumCapacity);
    public abstract void freeMutableInstance(U mutable);

    private final CapacityMax capacityMax;

    MutableInstanceAllocator(CapacityMax capacityMax) {

        this.capacityMax = Objects.requireNonNull(capacityMax);
    }

    @Override
    public final T createMutable(long minimumCapacity) {

        checkCreateMutableParameters(capacityMax, minimumCapacity);

        @SuppressWarnings("unchecked")
        final T result = (T)allocateMutableInstance(minimumCapacity);

        return result;
    }

    @Override
    public final void freeMutable(T mutable) {

        checkFreeCreatedMutableParameters(mutable);

        @SuppressWarnings("unchecked")
        final U mutableElements = (U)mutable;

        freeMutableInstance(mutableElements);
    }

    final CapacityMax getCapacityMax() {
        return capacityMax;
    }
}
