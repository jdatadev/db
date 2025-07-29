package dev.jdata.db.utils.jdk.allocators;

import java.util.List;

public interface IListAllocator {

    <T> List<T> allocateList(int minimumCapacity);

    void freeList(List<?> list);
}
