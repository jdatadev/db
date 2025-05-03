package dev.jdata.db.utils.allocators;

public interface IInstanceAllocator<T> extends IAllocator {

    long getNumCurrentlyAllocatedInstances();
    long getNumFreeListInstances();

    long getTotalNumAllocatedInstances();
    long getTotalNumFreedInstances();
}
