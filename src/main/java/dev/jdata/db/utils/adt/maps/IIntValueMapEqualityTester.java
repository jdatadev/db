package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntValueMapEqualityTester<P1, P2, E extends Exception> extends IValueMapEqualityTesterMarker<P1, P2, E> {

    boolean equals(int element1, P1 parameter1, int element2, P2 parameter2) throws E;
}
