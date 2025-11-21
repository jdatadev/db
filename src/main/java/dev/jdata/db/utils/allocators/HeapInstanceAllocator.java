package dev.jdata.db.utils.allocators;

abstract class HeapInstanceAllocator implements IInstanceAllocationTracking {

    @Override
    public final long getNumCurrentlyAllocatedInstances() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final long getNumFreeListInstances() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final long getTotalNumAllocatedInstances() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final long getTotalNumFreedInstances() {

        throw new UnsupportedOperationException();
    }
}
