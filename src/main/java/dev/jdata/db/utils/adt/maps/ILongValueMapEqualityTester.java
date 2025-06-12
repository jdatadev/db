package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IEqualityTester;

@FunctionalInterface
public interface ILongValueMapEqualityTester<P1, P2> extends IEqualityTester<P1, P2> {

    boolean equals(long element1, P1 parameter1, long element2, P2 parameter2);
}
