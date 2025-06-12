package dev.jdata.db.utils.adt.maps;

interface IObjectKeyStaticMapRemovalMutators<K> extends IKeyStaticMapRemovalMutators {

    void remove(K key);
}
