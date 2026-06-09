package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface ICharForEach2<P1, P2, E extends Exception> {

    void each(char element, P1 parameter1, P2 parameter2) throws E;
}
