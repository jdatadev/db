package dev.jdata.db.utils.adt.maps;

interface IObjectContainsKeyMap<K> extends IKeyMap<K[]> {

    boolean containsKey(K key);
}
