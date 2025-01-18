package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseElements {

    protected static int intIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index);
    }

    protected static int intNumElements(long numElements) {

        return Integers.checkUnsignedLongToUnsignedInt(numElements);
    }
}
