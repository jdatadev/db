package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IEqualityTester;

@FunctionalInterface
public interface IIntValueMapEqualityTester<P1, P2> extends IEqualityTester<P1, P2> {

    boolean equals(int element1, P1 parameter1, int element2, P2 parameter2);
}
