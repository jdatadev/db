package dev.jdata.db.utils.adt.maps;

interface IIntToObjectStaticMapRemovalMutators<V> extends IKeyValueStaticMapRemovalMutatorsMarker {

    V removeAndReturnPrevious(int key);
}
