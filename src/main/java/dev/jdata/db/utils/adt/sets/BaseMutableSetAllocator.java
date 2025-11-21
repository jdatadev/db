package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.allocators.BaseArrayAllocator;

public abstract class BaseMutableSetAllocator<T extends IBaseMutableSet> extends BaseArrayAllocator<T> {

    BaseMutableSetAllocator(IntFunction<? extends T> createSet, ToIntFunction<T> arrayLengthGetter) {
        super(createSet, arrayLengthGetter);
    }

    final T allocateMutableSet(int minimumCapacityExponent) {

        return allocateArrayInstance(minimumCapacityExponent);
    }

    final void freeMutableSet(T set) {

        freeArrayInstance(set);
    }
}
