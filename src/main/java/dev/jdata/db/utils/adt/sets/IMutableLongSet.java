package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongSet extends IBaseMutableLongSet {
heap
    public static IMutableLongSet create(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableLongMaxDistanceNonBucketSet(CapacityExponents.computeCapacityExponent(initialCapacity));
    }
}
