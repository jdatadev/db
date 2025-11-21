package dev.jdata.db.utils.allocators;

final class BuilderAllocationTracking extends InstanceAllocationTracking implements IBuilderAllocationTracking {

    private long numCurrentlyAllocatedBuilders;
    private long numFreeListBuilders;

    private long totalNumAllocatedBuilders;
    private long totalNumFreedBuilders;

    BuilderAllocationTracking() {

        this.numCurrentlyAllocatedBuilders = 0L;
        this.numFreeListBuilders = 0L;

        this.totalNumAllocatedBuilders = 0L;
        this.totalNumFreedBuilders = 0L;
    }

    @Override
    public final long getNumCurrentlyAllocatedBuilders() {
        return numCurrentlyAllocatedBuilders;
    }

    @Override
    public final long getNumFreeListBuilders() {
        return numFreeListBuilders;
    }

    @Override
    public final long getTotalNumAllocatedBuilders() {
        return totalNumAllocatedBuilders;
    }

    @Override
    public final long getTotalNumFreedBuilders() {
        return totalNumFreedBuilders;
    }

    final void addAllocatedBuilder(boolean fromFreeList) {

        ++ numCurrentlyAllocatedBuilders;
        ++ totalNumAllocatedBuilders;

        if (fromFreeList) {

            if (numFreeListBuilders == 0L) {

                throw new IllegalStateException();
            }

            -- numFreeListBuilders;
        }
    }

    final void addFreedBuilder(boolean toFreeList) {

        if (numCurrentlyAllocatedBuilders == 0L) {

            throw new IllegalStateException();
        }

        -- numCurrentlyAllocatedBuilders;

        if (toFreeList) {

            ++ numFreeListBuilders;
        }

        ++ totalNumFreedBuilders;
    }
}
