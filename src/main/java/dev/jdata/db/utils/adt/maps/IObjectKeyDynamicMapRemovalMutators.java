package dev.jdata.db.utils.adt.maps;

interface IObjectKeyDynamicMapRemovalMutators<T> extends IKeyDynamicMapRemovalMutatorsMarker {

    boolean remove(T key);
}
