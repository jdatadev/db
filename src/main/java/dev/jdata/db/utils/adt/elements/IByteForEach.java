package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IByteForEach<P, E extends Exception> {

    void each(byte element, P parameter) throws E;
}
