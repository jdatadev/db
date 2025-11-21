package dev.jdata.db.utils.adt.sets;

interface IObjectSetMutators<T> extends ISetMutatorsMarker {

    boolean addToSet(T value);
}
