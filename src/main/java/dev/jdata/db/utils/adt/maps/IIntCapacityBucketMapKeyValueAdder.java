package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntCapacityBucketMapKeyValueAdder<SRC_BUCKETS, KEYS_DST, VALUES_DST> extends IKeyValueAdder {

    void addValue(long srcNode, SRC_BUCKETS srcBuckets, int dstIndex, KEYS_DST keysDst, VALUES_DST valuesDst);
}
