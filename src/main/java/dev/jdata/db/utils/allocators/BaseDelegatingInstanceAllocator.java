package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.review.IDeprecatedInstanceAllocator;

abstract class BaseDelegatingInstanceAllocator<T, U extends TrackingInstanceAllocator> implements IDeprecatedInstanceAllocator<T> {

    private final U delegate;

    BaseDelegatingInstanceAllocator(U delegate) {

        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public final long getNumCurrentlyAllocatedInstances() {

        return delegate.getNumCurrentlyAllocatedInstances();
    }

    @Override
    public final long getNumFreeListInstances() {

        return delegate.getNumFreeListInstances();
    }

    @Override
    public final long getTotalNumAllocatedInstances() {

        return delegate.getTotalNumAllocatedInstances();
    }

    @Override
    public final long getTotalNumFreedInstances() {

        return delegate.getTotalNumFreedInstances();
    }

    protected final void addAllocatedInstance(boolean fromFreeList) {

        getDelegate().addAllocatedInstance(fromFreeList);
    }

    protected final void addFreedInstance(boolean toFreeList) {

        getDelegate().addFreedInstance(toFreeList);
    }

    final U getDelegate() {
        return delegate;
    }
}
