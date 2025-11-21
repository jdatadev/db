package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IIntForEach<P, E extends Exception> {

    void each(int element, P parameter) throws E;
}
