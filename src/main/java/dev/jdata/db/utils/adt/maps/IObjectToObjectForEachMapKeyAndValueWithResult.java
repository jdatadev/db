package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IObjectToObjectForEachMapKeyAndValueWithResult<K, V, P1, P2, R, E extends Exception> extends IForEachMapKeyAndValueWithResultMarker {

    R each(K key, V value, P1 parameter1, P2 parameter2) throws E;
}
