package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElements.IElementEqualityTester;

@FunctionalInterface
public interface IObjectValueMapEqualityTester<T, P1, P2> extends IElementEqualityTester<T, P1, P2> {

}
