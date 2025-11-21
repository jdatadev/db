package dev.jdata.db.utils.jdk.allocators;

import java.util.List;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.BaseIntCapacityInstanceAllocator;
import dev.jdata.db.utils.jdk.adt.lists.ArrayListImpl;

public final class ListAllocator extends BaseIntCapacityInstanceAllocator<ArrayListImpl<?>> implements IListAllocator {

    public ListAllocator(IntFunction<Object[]> createArray) {
        super(createArray, ArrayListImpl::new, l -> l.getCapacity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> allocateList(int minimumCapacity) {

        return (List<T>)allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);
    }

    @Override
    public void freeList(List<?> list) {

        freeArrayInstance((ArrayListImpl<?>)list);
    }
}
