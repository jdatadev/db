package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IObjectForEach2<T, P1, P2, E extends Exception> {

    void each(T element, P1 parameter1, P2 parameter2) throws E;
}
