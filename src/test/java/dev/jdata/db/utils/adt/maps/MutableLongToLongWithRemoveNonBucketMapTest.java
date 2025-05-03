package dev.jdata.db.utils.adt.maps;

public final class MutableLongToLongWithRemoveNonBucketMapTest extends BaseMutableLongToLongNonContainsKeyNonBucketMapTest<MutableLongToLongWithRemoveNonBucketMap> {

    @Override
    MutableLongToLongWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new MutableLongToLongWithRemoveNonBucketMap(initialCapacityExponent);
    }

    @Override
    boolean remove(MutableLongToLongWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
