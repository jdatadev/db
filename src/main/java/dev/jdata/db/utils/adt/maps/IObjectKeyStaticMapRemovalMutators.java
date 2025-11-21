package dev.jdata.db.utils.adt.maps;

interface IObjectKeyStaticMapRemovalMutators<K> extends IKeyStaticMapRemovalMutatorsMarker {

    void remove(K key);
}
