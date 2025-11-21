package dev.jdata.db.utils.adt.byindex;

interface IObjectByIndexGetters<T> extends IByIndexGettersMarker {

    T get(long index);

    boolean containsInstance(T instance, long startIndex, long numElements);
}
