package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.function.ObjLongFunction;

@Deprecated // allocator for AllocatorNode to avoid linked list objects
public final class CapacityInstanceAllocator<T extends Allocatable> extends BaseCapacityInstanceAllocator<T> {

    @Deprecated
    public <P> CapacityInstanceAllocator(CapacityMax capacityMax, P parameter, ObjLongFunction<P, T> createInstance, ToLongFunction<T> capacityGetter) {
        super(capacityMax, parameter, createInstance, capacityGetter);
    }

    @Deprecated
    public T allocateCapacityInstance(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(getCapacityMax(), minimumCapacity);

        final T result = allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);

        result.setAllocated(true);

        return result;
    }

    @Deprecated
    public void freeCapacityInstance(T instance) {

        Objects.requireNonNull(instance);

        instance.setAllocated(false);

        freeArrayInstance(instance);
    }
}
