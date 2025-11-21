package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.allocators.BaseIntCapacityInstanceAllocator;

abstract class ArrayAllocator<T> extends BaseIntCapacityInstanceAllocator<T> {

    ArrayAllocator(IntFunction<T> createInstance, ToIntFunction<T> capacityGetter) {
        super(createInstance, (create, capacity) -> create.apply(capacity), a -> capacityGetter.applyAsInt(a), true);
    }
}
