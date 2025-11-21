package dev.jdata.db.utils.allocators;

interface IBuilderAllocationTracking extends IInstanceAllocationTracking {

    long getNumCurrentlyAllocatedBuilders();
    long getNumFreeListBuilders();

    long getTotalNumAllocatedBuilders();
    long getTotalNumFreedBuilders();
}
