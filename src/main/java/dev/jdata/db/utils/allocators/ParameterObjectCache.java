package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.FreeList;

final class ParameterObjectCache<T, P> {

    private final Function<P, T> allocator;
    private final FreeList<T> freeList;

    ParameterObjectCache(Function<P, T> allocator, IntFunction<T[]> createArray) {

        Objects.requireNonNull(allocator);
        Objects.requireNonNull(createArray);

        this.allocator = allocator;

        this.freeList = new FreeList<>(createArray);
    }

    T allocate(P parameter) {

        T allocated = freeList.allocate();

        if (allocated == null) {

            allocated = allocator.apply(parameter);
        }

        return allocated;
    }

    void free(T instance) {

        Objects.requireNonNull(instance);

        freeList.free(instance);
    }
}
