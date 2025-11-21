package dev.jdata.db.utils.adt.maps;

interface IObjectCountMapGetters<T> {

    long getCount(T key);

    <E extends Exception> void forEach(IObjectForEachCountMapElement<T, E> forEachElement) throws E;
}
