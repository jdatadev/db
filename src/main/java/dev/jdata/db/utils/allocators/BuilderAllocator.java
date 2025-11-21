package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IBuilder;

public abstract class BuilderAllocator<T, U extends IBuilder> extends BaseDelegatingInstanceAllocator<T, BuilderTrackingAllocator<U>> implements IBuilderAllocator<U> {

    protected BuilderAllocator() {
        super(new BuilderTrackingAllocator<U>() { });
    }

    protected final void addAllocatedBuilder(boolean fromFreeList) {

        getDelegate().addAllocatedBuilder(fromFreeList);
    }

    protected final void addFreedBuilder(boolean toFreeList) {

        getDelegate().addFreedBuilder(toFreeList);
    }

    @Override
    public final long getNumCurrentlyAllocatedBuilders() {

        return getDelegate().getNumCurrentlyAllocatedBuilders();
    }

    @Override
    public final long getNumFreeListBuilders() {

        return getDelegate().getNumFreeListBuilders();
    }

    @Override
    public final long getTotalNumAllocatedBuilders() {

        return getDelegate().getTotalNumAllocatedBuilders();
    }

    @Override
    public final long getTotalNumFreedBuilders() {

        return getDelegate().getTotalNumFreedBuilders();
    }
}
