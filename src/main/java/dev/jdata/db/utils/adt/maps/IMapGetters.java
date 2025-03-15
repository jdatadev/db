package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElements;

public interface IMapGetters<K, V> extends IElements {

    V get(K key);
}
