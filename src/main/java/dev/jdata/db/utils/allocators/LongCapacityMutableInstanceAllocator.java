package dev.jdata.db.utils.allocators;

import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;

public abstract class LongCapacityMutableInstanceAllocator<T extends IMutable, U extends Allocatable, V> extends CapacityMutableInstanceAllocator<T, U, V> {

    protected LongCapacityMutableInstanceAllocator(LongFunction<V> createElements, ToLongFunction<U> capacityGetter) {
        super(CapacityMax.LONG, createElements, capacityGetter);
    }
}
