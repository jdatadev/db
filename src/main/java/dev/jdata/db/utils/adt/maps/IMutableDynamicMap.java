package dev.jdata.db.utils.adt.maps;

public interface IMutableDynamicMap<K, V>

        extends IMutableMap<K, V>, IObjectToObjectDynamicMapCommon<K, V>, IObjectKeyDynamicMapRemovalMutators<K>, IObjectToObjectDynamicMapRemovalMutators<K, V> {

}