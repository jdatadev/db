package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public abstract class BaseAllocatableArrayAllocator<T extends Allocatable> extends BaseArrayAllocator<T> {

    protected BaseAllocatableArrayAllocator(IntFunction<T> createArray, ToIntFunction<T> arrayLengthGetter) {
        super(createArray, arrayLengthGetter);
    }

    protected final T allocateAllocatableArrayInstance(int minimumCapacity) {

        final T result = allocateArrayInstance(minimumCapacity);

        result.setAllocated(true);

        return result;
    }

    protected final void freeAllocatableArrayInstance(T instance) {

        instance.setAllocated(false);

        freeArrayInstance(instance);
    }
}
