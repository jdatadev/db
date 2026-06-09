package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

abstract class LongCapacityHeapMutableInstanceAllocator<T extends IMutable, U extends IMutableFrom> extends HeapMutableInstanceAllocator<T, U> {

    static final CapacityMax CAPACITY_MAX = CapacityMax.LONG;

    protected static void checkAllocateMutableParameters(long minimumCapacity) {

        checkMinimumCapacity(CAPACITY_MAX, minimumCapacity);
    }
}
