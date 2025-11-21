package dev.jdata.db.utils.adt.numbers;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseIntCapacityInstanceAllocator;

public abstract class CachedMutableLargeNumberAllocator<T extends BaseNumber<T> & IMutableLargeNumber> extends BaseIntCapacityInstanceAllocator<T> {

    @FunctionalInterface
    protected interface IntCapacityAllocator<R> {

        R allocate(AllocationType allocationType, int minimumCapacity);
    }

    protected CachedMutableLargeNumberAllocator(IntCapacityAllocator<T> createLargeInteger) {
        super(createLargeInteger, (create, precision) -> create.allocate(AllocationType.CACHING_ALLOCATOR, precision), IMutableLargeNumber::getPrecision);
    }
}
