package dev.jdata.db.utils.adt.maps;

interface IObjectContainsKeyMapGetters<K> extends IContainsKeyMapMarker {

    boolean containsKey(K key);
}
