package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IBuilder;

public interface IBuilderAllocator<T extends IBuilder> extends IInstanceAllocator {

    long getNumCurrentlyAllocatedBuilders();
    long getNumFreeListBuilders();

    long getTotalNumAllocatedBuilders();
    long getTotalNumFreedBuilders();
}
