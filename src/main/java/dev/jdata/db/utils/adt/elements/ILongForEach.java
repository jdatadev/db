package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface ILongForEach<P, E extends Exception> {

    void each(long element, P parameter) throws E;
}
