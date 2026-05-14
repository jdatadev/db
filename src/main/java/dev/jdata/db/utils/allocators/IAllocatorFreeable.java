package dev.jdata.db.utils.allocators;

public interface IAllocatorFreeable<T> {

    void free(T allocator);
}
