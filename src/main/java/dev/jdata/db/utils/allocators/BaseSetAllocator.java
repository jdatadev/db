package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.sets.ISet;

abstract class BaseSetAllocator<T extends ISet> extends BaseArrayAllocator<T> {

    BaseSetAllocator(IntFunction<T> createSet, ToIntFunction<T> arrayLengthGetter) {
        super(createSet, arrayLengthGetter, false);
    }

    final T allocateSet(int minimumCapacityExponent) {

        return allocateArrayInstance(minimumCapacityExponent);
    }

    final void freeSet(T set) {

        freeArrayInstance(set);
    }
}
