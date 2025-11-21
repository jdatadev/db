package dev.jdata.db.utils.adt.sets;

abstract class LongBucketSet extends BaseLongBucketSet implements ILongSet {

    LongBucketSet(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }
}
