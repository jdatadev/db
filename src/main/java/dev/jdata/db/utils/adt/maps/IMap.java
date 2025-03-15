package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMap<K, V> extends IMapGetters<K, V>, IClearable {

    void put(K key, V value);
}
