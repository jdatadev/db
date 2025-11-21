package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IObjectForEach<T, P, E extends Exception> {

    void each(T element, P parameter) throws E;
}
