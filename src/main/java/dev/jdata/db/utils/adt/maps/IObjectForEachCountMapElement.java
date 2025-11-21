package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IObjectForEachCountMapElement<T, E extends Exception> {

    void each(T value, int count) throws E;
}
