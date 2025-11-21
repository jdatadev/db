package dev.jdata.db.utils.adt.maps;

interface IIntKeyDynamicMapRemovalMutators extends IKeyDynamicMapRemovalMutatorsMarker {

    boolean remove(int key);
}
