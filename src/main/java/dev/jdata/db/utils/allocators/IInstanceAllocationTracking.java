package dev.jdata.db.utils.allocators;

interface IInstanceAllocationTracking {

    long getNumCurrentlyAllocatedInstances();
    long getNumFreeListInstances();

    long getTotalNumAllocatedInstances();
    long getTotalNumFreedInstances();
}
