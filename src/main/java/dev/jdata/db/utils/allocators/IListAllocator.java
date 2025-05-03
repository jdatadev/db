package dev.jdata.db.utils.allocators;

import java.util.List;

@Deprecated
public interface IListAllocator {

    <T> List<T> allocateList(int minimumCapacity);

    void freeList(List<?> list);
}
