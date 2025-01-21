package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.Contains;
import dev.jdata.db.utils.scalars.Integers;

public interface Elements extends Contains {

    public static int intIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index);
    }

    public static int intNumElements(long numElements) {

        return Integers.checkUnsignedLongToUnsignedInt(numElements);
    }

    long getNumElements();
}
