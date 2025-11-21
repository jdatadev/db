package dev.jdata.db.utils.adt.lists;

interface IObjectTailListRemovalMutators<T> extends IListMutatorsMarker {

    T removeTailAndReturnValue();
}
