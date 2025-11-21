package dev.jdata.db.utils.allocators;

import java.util.Objects;

abstract class BaseTrackingInstanceAllocator<T, U extends InstanceAllocationTracking> implements ITypedInstanceAllocator<T> {

    private final U delegate;

    BaseTrackingInstanceAllocator(U delegate) {

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
