package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

public final class ArrayAllocator<T> extends BaseArrayAllocator<T[]> implements IArrayAllocator<T> {

    public ArrayAllocator(IntFunction<T[]> createArray) {
        super(createArray, a -> a.length);
    }

    @Override
    public T[] allocateArray(int minimumCapacity) {

        return allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeArray(T[] array) {

        freeArrayInstance(array);
    }
}
