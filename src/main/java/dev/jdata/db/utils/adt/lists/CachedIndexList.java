package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;

public final class CachedIndexList<T> extends IndexList<T> implements ICachedIndexList<T> {

    static final class IndexListArrayAllocator<T, U extends IndexList<T>> extends BaseAllocatableArrayAllocator<U> {

        IndexListArrayAllocator(AllocationType allocationType, IntFunction<U> createList) {
            super(createList, l -> IElementsView.intNumElements(l.getNumElements()));
        }

        U allocateIndexList(int minimumCapacity) {

            return allocateAllocatableArrayInstance(minimumCapacity);
        }

        void freeIndexList(U list) {

            freeAllocatableArrayInstance(list);
        }
    }

    static final CachedIndexList<?> emptyList = new CachedIndexList<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    public static <T> CachedIndexList<T> empty() {

        return (CachedIndexList<T>)emptyList;
    }

    private CachedIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    @Override
    public HeapIndexList<T> toHeapAllocated() {

        return new HeapIndexList<>(this);
    }
}
