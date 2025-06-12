package dev.jdata.db.utils.adt.maps;

interface IIntKeyDynamicMapRemovalMutators extends IKeyDynamicMapRemovalMutators {

    boolean remove(int key);
}
