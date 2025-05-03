package dev.jdata.db.utils.adt.maps;

public interface IMutableObjectWithRemoveNonBucketMap<K, V> extends IMutableMap<K, V>, IObjectNonContainsKeyNonBucketMapMutators<K>, IObjectMapGetters<K, V>,
        IObjectNonContainsKeyNonBucketMapGetters<K, V> {

}
