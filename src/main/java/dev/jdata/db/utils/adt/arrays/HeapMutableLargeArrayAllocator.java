package dev.jdata.db.utils.adt.arrays;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.capacity.ICapacity;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.LongCapacityMutableInstanceAllocator;

abstract class HeapMutableLargeArrayAllocator<T extends IMutable, U extends Allocatable & ICapacity, V extends IMutableFrom>

        extends LongCapacityMutableInstanceAllocator<T, U, Void, V> {

    abstract U allocateMutableLargeArray(long minimumCapacity);

    HeapMutableLargeArrayAllocator() {
        super(c -> null, ICapacity::getCapacity);
    }

    @Override
    protected final U allocateMutableInstance(LongFunction<Void> createElements, long minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return allocateMutableLargeArray(minimumCapacity);
    }
}
