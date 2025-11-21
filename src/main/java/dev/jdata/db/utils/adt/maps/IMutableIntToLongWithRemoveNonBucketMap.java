package dev.jdata.db.utils.adt.maps;

public interface IMutableIntToLongWithRemoveNonBucketMap extends IMutableIntToLongStaticMap {

    public static IMutableIntToLongWithRemoveNonBucketMap create(int initialCapacityExponent) {

        return new MutableIntToLongWithRemoveNonBucketMap(initialCapacityExponent);
    }
}
