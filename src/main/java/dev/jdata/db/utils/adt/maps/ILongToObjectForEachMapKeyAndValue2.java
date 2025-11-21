package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface ILongToObjectForEachMapKeyAndValue2<V, P1, P2, E extends Exception> extends IForEachMapKeyAndValue2Marker {

    void each(long key, V value, P1 parameter1, P2 parameter2) throws E;
}
