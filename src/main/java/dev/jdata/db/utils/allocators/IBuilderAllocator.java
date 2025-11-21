package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IBuilder;

public interface IBuilderAllocator<T extends IBuilder> extends IBuilderAllocationTracking {

    void freeBuilder(T builder);
}
