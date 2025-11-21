package dev.jdata.db.utils.adt.maps;

interface ILongKeyDynamicMapRemovalMutators extends IKeyDynamicMapRemovalMutatorsMarker {

    boolean remove(long key);
}
