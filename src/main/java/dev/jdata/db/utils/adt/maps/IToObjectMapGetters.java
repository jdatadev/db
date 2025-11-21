package dev.jdata.db.utils.adt.maps;

interface IToObjectMapGetters<V> extends IToValueMapGettersMarker {

    <P, E extends Exception> void forEachValue(P parameter, IObjectForEachMapValue<V, P, E> forEach) throws E;
}
