package dev.jdata.db.utils.adt.sets;

final class HeapLongBucketSet extends LongBucketSet implements IHeapLongSet {

    HeapLongBucketSet(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }
}
