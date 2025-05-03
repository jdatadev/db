package dev.jdata.db.utils.allocators;

public interface IFreeable<T> {

    void free(T allocator);
}
