package dev.jdata.db.utils.allocators;

abstract class InstanceAllocator<T> implements IInstanceAllocator<T> {

    private long numCurrentlyAllocatedInstances;
    private long numFreeListInstances;

    private long totalNumAllocatedInstances;
    private long totalNumFreedInstances;

    InstanceAllocator() {

        this.numCurrentlyAllocatedInstances = 0L;
        this.numFreeListInstances = 0L;

        this.totalNumAllocatedInstances = 0L;
        this.totalNumFreedInstances = 0L;
    }

    protected final void addAllocatedInstance(boolean fromFreeList) {

        ++ numCurrentlyAllocatedInstances;
        ++ totalNumAllocatedInstances;

        if (fromFreeList) {

            if (numFreeListInstances == 0L) {

                throw new IllegalStateException();
            }

            -- numFreeListInstances;
        }
    }

    protected final void addFreedInstance(boolean toFreeList) {

        if (numCurrentlyAllocatedInstances == 0L) {

            throw new IllegalStateException();
        }

        -- numCurrentlyAllocatedInstances;

        if (toFreeList) {

            ++ numFreeListInstances;
        }

        ++ totalNumFreedInstances;
    }

    @Override
    public final long getNumCurrentlyAllocatedInstances() {
        return numCurrentlyAllocatedInstances;
    }

    @Override
    public final long getNumFreeListInstances() {
        return numFreeListInstances;
    }

    @Override
    public final long getTotalNumAllocatedInstances() {
        return totalNumAllocatedInstances;
    }

    @Override
    public final long getTotalNumFreedInstances() {
        return totalNumFreedInstances;
    }
}
