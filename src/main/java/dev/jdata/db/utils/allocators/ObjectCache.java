package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer;

public final class ObjectCache<T> implements IObjectCache<T>, IInstanceAllocator<T> {

    private final ParameterObjectCache<T, Void> delegate;

    public ObjectCache(Supplier<T> allocator, IntFunction<T[]> createArray) {

        Objects.requireNonNull(allocator);
        Objects.requireNonNull(createArray);

        this.delegate = new ParameterObjectCache<>(p -> allocator.get(), createArray);
    }

    @Override
    public long getNumCurrentlyAllocatedInstances() {

        return delegate.getNumCurrentlyAllocatedInstances();
    }

    @Override
    public long getNumFreeListInstances() {

        return delegate.getNumFreeListInstances();
    }

    @Override
    public long getTotalNumAllocatedInstances() {

        return delegate.getTotalNumAllocatedInstances();
    }

    @Override
    public long getTotalNumFreedInstances() {

        return delegate.getTotalNumFreedInstances();
    }

    @Override
    public void addCache(IAllocatorsStatisticsGatherer statisticsGatherer, String name, Class<T> objectType) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addObjectCache(name, objectType, this);
    }

    public T allocate() {

        return delegate.allocate(null);
    }

    public void free(T instance) {

        Objects.requireNonNull(instance);

        delegate.free(instance);
    }
}
