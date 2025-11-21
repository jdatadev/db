package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IObjectToObjectForEachMapKeyAndValue<K, V, P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(K key, V value, P parameter) throws E;
}
