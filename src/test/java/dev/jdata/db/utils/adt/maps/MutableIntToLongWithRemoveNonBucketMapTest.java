package dev.jdata.db.utils.adt.maps;

public final class MutableIntToLongWithRemoveNonBucketMapTest extends BaseMutableIntToLongNonContainsKeyNonBucketMapTest<MutableIntToLongWithRemoveNonBucketMap> {

    @Override
    MutableIntToLongWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new MutableIntToLongWithRemoveNonBucketMap(initialCapacityExponent);
    }

    @Override
    boolean remove(MutableIntToLongWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
