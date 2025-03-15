package dev.jdata.db.utils.adt.maps;

public interface ObjectKeyMap<K> extends KeyMap<K[]> {

    boolean containsKey(K key);

    boolean remove(K key);
}
