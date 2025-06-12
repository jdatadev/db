package dev.jdata.db.utils.adt.maps;

interface ILongKeyDynamicMapRemovalMutators extends IKeyDynamicMapRemovalMutators {

    boolean remove(long key);
}
