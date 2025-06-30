package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IObjectBuilder;

public abstract class BuilderInstanceAllocator<T, U extends IObjectBuilder<T, U>> extends BaseInstanceAllocator<T, BuilderElementAllocator<U>> implements IBuilderInstanceAllocator<T, U> {

    protected BuilderInstanceAllocator() {
        super(new BuilderElementAllocator<U>() { });
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
