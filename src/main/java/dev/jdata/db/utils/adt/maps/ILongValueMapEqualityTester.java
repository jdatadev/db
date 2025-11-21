package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface ILongValueMapEqualityTester<P1, P2, E extends Exception> extends IValueMapEqualityTesterMarker<P1, P2, E> {

    boolean equals(long element1, P1 parameter1, long element2, P2 parameter2) throws E;
}
