package dev.jdata.db.utils.adt.maps;

interface IToIntMapGetters extends IToValueMapGettersMarker {

    <P, E extends Exception> void forEachValue(P parameter, IIntForEachMapValue<P, E> forEach) throws E;
}
