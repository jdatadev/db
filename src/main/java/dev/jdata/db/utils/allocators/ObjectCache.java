package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public final class ObjectCache<T> {

    private final ParameterObjectCache<T, Void> delegate;

    public ObjectCache(Supplier<T> allocator, IntFunction<T[]> createArray) {

        Objects.requireNonNull(allocator);
        Objects.requireNonNull(createArray);

        this.delegate = new ParameterObjectCache<>(p -> allocator.get(), createArray);
    }

    public T allocate() {

        return delegate.allocate(null);
    }

    public void free(T instance) {

        Objects.requireNonNull(instance);

        delegate.free(instance);
    }
}
