package dev.jdata.db.utils.adt.maps;

public interface IMutableWithRemoveStaticMap<K, V> extends IMutableStaticMap<K, V>, IObjectKeyStaticMapRemovalMutators<K>, IObjectToObjectStaticMapRemovalMutators<K, V> {

}
