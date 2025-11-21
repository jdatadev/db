package dev.jdata.db.utils.adt.lists;

interface IObjectNodeListGetters<T> extends IListGettersMarker {

    T getValue(long node);
}
