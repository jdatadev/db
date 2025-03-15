package dev.jdata.db.utils.allocators;

import java.util.List;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.scalars.Integers;

public final class ListAllocator extends BaseArrayAllocator<IndexList<?>> implements IListAllocator {

    public ListAllocator(IntFunction<Object[]> createArray) {
        super(c -> new IndexList<Object>(createArray, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getCapacity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> allocateList(int minimumCapoacity) {

        return (IndexList<T>)allocateArrayInstance(minimumCapoacity);
     }

    @Override
    public void freeList(List<?> list) {

        freeArrayInstance((IndexList<?>)list);
    }
}
