package dev.jdata.db.utils.adt.maps;

public interface IMutableStaticMap<K, V>

        extends IMutableMap<K, V>, IObjectToObjectBaseStaticMapCommon<K, V>, IObjectKeyStaticMapRemovalMutators<K>, IObjectToObjectStaticMapRemovalMutators<K, V> {
}
