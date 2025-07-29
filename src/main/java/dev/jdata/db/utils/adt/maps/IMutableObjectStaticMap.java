package dev.jdata.db.utils.adt.maps;

public interface IMutableObjectStaticMap<K, V>

        extends IMutableCommonMap<K, V, IObjectToObjectStaticMapCommon<K, V>>,
                IObjectToObjectStaticMapCommon<K, V>,
                IObjectKeyStaticMapRemovalMutators<K>,
                IObjectToObjectStaticMapRemovalMutators<K, V> {
}
