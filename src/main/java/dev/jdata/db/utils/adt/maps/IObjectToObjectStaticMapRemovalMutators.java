package dev.jdata.db.utils.adt.maps;

public interface IObjectToObjectStaticMapRemovalMutators<K, V> extends IKeyValueStaticMapRemovalMutators {

    V removeAndReturnPrevious(K key);
}
