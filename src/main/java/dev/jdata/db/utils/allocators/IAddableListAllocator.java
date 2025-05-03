package dev.jdata.db.utils.allocators;

import org.jutils.ast.objects.list.IAddableList;

public interface IAddableListAllocator {

    <T> IAddableList<T> allocateList(int minimumCapacity);

    void freeList(IAddableList<?> list);
}
