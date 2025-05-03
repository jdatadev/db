package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.FreeList;

final class ParameterObjectCache<T, P> extends BaseObjectAllocator<T> {

    private final Function<P, T> allocator;
    private final FreeList<T> freeList;

    private long numAllocatedInstances;

    ParameterObjectCache(Function<P, T> allocator, IntFunction<T[]> createArray) {

        Objects.requireNonNull(allocator);
        Objects.requireNonNull(createArray);

        this.allocator = allocator;

        this.freeList = new FreeList<>(createArray);

        this.numAllocatedInstances = 0L;
    }

    T allocate(P parameter) {

        T allocated = freeList.allocate();

        if (allocated == null) {

            allocated = allocator.apply(parameter);
        }

        ++ numAllocatedInstances;

        return allocated;
    }

    void free(T instance) {

        Objects.requireNonNull(instance);

        if (numAllocatedInstances == 0L) {

            throw new IllegalStateException();
        }

        -- numAllocatedInstances;

         freeList.free(instance);
    }
}
