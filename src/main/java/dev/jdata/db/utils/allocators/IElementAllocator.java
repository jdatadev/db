package dev.jdata.db.utils.allocators;

public interface IElementAllocator extends IAllocator {

    long getNumCurrentlyAllocatedInstances();
    long getNumFreeListInstances();

    long getTotalNumAllocatedInstances();
    long getTotalNumFreedInstances();
}
