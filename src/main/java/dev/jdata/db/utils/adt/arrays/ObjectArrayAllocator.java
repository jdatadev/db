package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

public final class ObjectArrayAllocator<T> extends ArrayAllocator<T[]> implements IArrayAllocator<T> {

    public ObjectArrayAllocator(IntFunction<T[]> createArray) {
        super(createArray, a -> a.length);
    }

    @Override
    public T[] allocateArray(int minimumCapacity) {

        return allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);
    }

    @Override
    public void freeArray(T[] array) {

        freeArrayInstance(array);
    }
}
