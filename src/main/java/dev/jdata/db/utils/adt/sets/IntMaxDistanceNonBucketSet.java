package dev.jdata.db.utils.adt.sets;

abstract class IntMaxDistanceNonBucketSet extends BaseIntMaxDistanceNonBucketSet implements IBaseIntSet {

    IntMaxDistanceNonBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    IntMaxDistanceNonBucketSet(AllocationType allocationType, BaseIntMaxDistanceNonBucketSet toCopy) {
        super(allocationType, toCopy);
    }
}
