package dev.jdata.db.utils.adt.maps;

public interface IObjectContainsKeyMap<K> extends IKeyMap<K[]> {

    boolean containsKey(K key);
}
