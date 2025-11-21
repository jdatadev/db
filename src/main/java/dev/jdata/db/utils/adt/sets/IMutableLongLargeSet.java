package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongLargeSet extends IBaseMutableLongSet<ILongLargeSet, ilongla> {
heap
    public static IMutableLongLargeSet create(int initialCapacityExponent) {

        Checks.isLongCapacityExponent(initialCapacityExponent);

        return new MutableLongLargeMaxDistanceNonBucketSet(initialCapacityExponent);
    }
}
