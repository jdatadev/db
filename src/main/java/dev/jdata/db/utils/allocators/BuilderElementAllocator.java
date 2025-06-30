package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IBuilder;

public abstract class BuilderElementAllocator<T extends IBuilder> extends ElementAllocator implements IBuilderAllocator<T> {

    private long numCurrentlyAllocatedBuilders;
    private long numFreeListBuilders;

    private long totalNumAllocatedBuilders;
    private long totalNumFreedBuilders;

    protected BuilderElementAllocator() {

        this.numCurrentlyAllocatedBuilders = 0L;
        this.numFreeListBuilders = 0L;

        this.totalNumAllocatedBuilders = 0L;
        this.totalNumFreedBuilders = 0L;
    }

    protected final void addAllocatedBuilder(boolean fromFreeList) {

        ++ numCurrentlyAllocatedBuilders;
        ++ totalNumAllocatedBuilders;

        if (fromFreeList) {

            if (numFreeListBuilders == 0L) {

                throw new IllegalStateException();
            }

            -- numFreeListBuilders;
        }
    }

    protected final void addFreedBuilder(boolean toFreeList) {

        if (numCurrentlyAllocatedBuilders == 0L) {

            throw new IllegalStateException();
        }

        -- numCurrentlyAllocatedBuilders;

        if (toFreeList) {

            ++ numFreeListBuilders;
        }

        ++ totalNumFreedBuilders;
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
}
