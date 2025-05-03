package dev.jdata.db.utils.allocators;

public interface IADTInstanceAllocator<T, U extends IBuilder<T, U>> extends IInstanceAllocator<T> {

    long getNumCurrentlyAllocatedBuilders();
    long getNumFreeListBuilders();

    long getTotalNumAllocatedBuilders();
    long getTotalNumFreedBuilders();
}
