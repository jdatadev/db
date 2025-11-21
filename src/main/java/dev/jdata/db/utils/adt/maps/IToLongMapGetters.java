package dev.jdata.db.utils.adt.maps;

interface IToLongMapGetters extends IToValueMapGettersMarker {

    <P, E extends Exception> void forEachValue(P parameter, ILongForEachMapValue<P, E> forEach) throws E;
}
