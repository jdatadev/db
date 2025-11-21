package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElementEqualityTester;

@FunctionalInterface
public interface IObjectValueMapEqualityTester<T, P1, P2, E extends Exception> extends IValueMapEqualityTesterMarker<P1, P2, E>, IElementEqualityTester<T, P1, P2, E> {

}
