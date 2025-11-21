package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntToObjectForEachMapKeyAndValue<T, P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(int key, T value, P parameter) throws E;
}
