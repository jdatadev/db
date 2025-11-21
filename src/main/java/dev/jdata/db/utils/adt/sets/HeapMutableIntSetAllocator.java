package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIntSetAllocator extends MutableIntSetAllocator<IHeapMutableIntSet> implements IHeapMutableIntSetAllocator {

    static final HeapMutableIntSetAllocator INSTANCE = new HeapMutableIntSetAllocator();

    @Override
    public IHeapMutableIntSet createMutable(long minimumCapacity) {

        Checks.isCapacity(minimumCapacity);

        final int intMinimumCapacity = Capacity.intCapacity(minimumCapacity);

        return new MutableIntMaxDistanceNonBucketSet(CapacityExponents.computeCapacityExponent(intMinimumCapacity));
    }
}
