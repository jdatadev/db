package dev.jdata.db.utils.adt.maps;

public final class MutableLongToIntWithRemoveNonBucketMapTest extends BaseMutableLongToIntNonContainsKeyNonBucketMapTest<MutableLongToIntWithRemoveNonBucketMap> {

    @Override
    MutableLongToIntWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new MutableLongToIntWithRemoveNonBucketMap(initialCapacityExponent);
    }

    @Override
    boolean remove(MutableLongToIntWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
