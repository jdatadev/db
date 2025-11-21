package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface ILongToObjectForEachMapKeyAndValue<V, P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(long key, V value, P parameter) throws E;
}
