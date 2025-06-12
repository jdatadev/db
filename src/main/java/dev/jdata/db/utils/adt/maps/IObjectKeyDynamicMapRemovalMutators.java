package dev.jdata.db.utils.adt.maps;

interface IObjectKeyDynamicMapRemovalMutators<T> extends IKeyDynamicMapRemovalMutators {

    boolean remove(T key);
}
