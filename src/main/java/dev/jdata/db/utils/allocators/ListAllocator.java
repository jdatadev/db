package dev.jdata.db.utils.allocators;

import java.util.List;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.ArrayListImpl;

@Deprecated
public final class ListAllocator extends BaseArrayAllocator<ArrayListImpl<?>> implements IListAllocator {

    public ListAllocator(IntFunction<Object[]> createArray) {
        super(c -> new ArrayListImpl<>(createArray, c), l -> l.getCapacity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> allocateList(int minimumCapacity) {

        return (List<T>)allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeList(List<?> list) {

        freeArrayInstance((ArrayListImpl<?>)list);
    }
}
