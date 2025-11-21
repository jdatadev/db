package dev.jdata.db.utils.adt.sets;

abstract class IntBucketSet extends BaseIntBucketSet implements IIntSet {

    IntBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    IntBucketSet(AllocationType allocationType, BaseIntBucketSet toCopy) {
        super(allocationType, toCopy);
    }
}
