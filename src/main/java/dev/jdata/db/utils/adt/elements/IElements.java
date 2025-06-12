package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.IEqualityTester;
import dev.jdata.db.utils.scalars.Integers;

public interface IElements extends IContains {

    @FunctionalInterface
    public interface IElementEqualityTester<T, P1, P2> extends IEqualityTester<P1, P2> {

        boolean equals(T element1, P1 parameter1, T element2, P2 parameter2);
    }

    public static int intIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index);
    }

    public static int intNumElements(long numElements) {

        return Integers.checkUnsignedLongToUnsignedInt(numElements);
    }

    long getNumElements();
}
