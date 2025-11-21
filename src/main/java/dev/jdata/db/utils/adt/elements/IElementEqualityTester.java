package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.marker.IEqualityTesterMarker;

@FunctionalInterface
public interface IElementEqualityTester<T, P1, P2, E extends Exception> extends IEqualityTesterMarker<P1, P2, E> {

    boolean equals(T element1, P1 parameter1, T element2, P2 parameter2) throws E;
}
