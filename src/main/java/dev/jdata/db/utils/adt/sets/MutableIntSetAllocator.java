package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableIntSetAllocator<T extends IMutableIntSet> extends BaseMutableSetAllocator<T> implements IBaseMutableIntSetAllocator<T> {

    MutableIntSetAllocator() {
        super(MutableIntMaxDistanceNonBucketSet::new, s -> Capacity.intCapacity(s.getCapacity()));
    }

    @Override
    public final IMutableIntSet allocateMutableIntSet(int minimumCapacityExponent) {

        Checks.isInitialIntCapacityExponent(minimumCapacityExponent);

        return allocateMutableSet(minimumCapacityExponent);
    }

    @Override
    public void freeMutableIntSet(IMutableIntSet intSet) {

        Objects.requireNonNull(intSet);

        freeMutableSet(intSet);
    }
}
