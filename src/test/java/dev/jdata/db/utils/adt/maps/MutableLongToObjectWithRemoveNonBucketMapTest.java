package dev.jdata.db.utils.adt.maps;

public final class MutableLongToObjectWithRemoveNonBucketMapTest

        extends BaseMutableLongToObjectNonContainsKeyNonBucketMapTest<Integer, MutableLongToObjectWithRemoveNonBucketMap<Integer>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    MutableLongToObjectWithRemoveNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new MutableLongToObjectWithRemoveNonBucketMap<>(initialCapacityExponent, Integer[]::new);
    }

    @Override
    Integer[] createValuesArray(int length) {

        return new Integer[length];
    }

    @Override
    boolean remove(MutableLongToObjectWithRemoveNonBucketMap<Integer> map, int key) {

        map.remove(key);

        return true;
    }

    @Override
    int objectToInt(Integer object) {

        return object.intValue();
    }

    @Override
    Integer intToObject(int integer) {

        return Integer.valueOf(integer);
    }
}
