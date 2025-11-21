package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.scalars.Integers;

public interface ICapacity {

    public static int intCapacity(long capacity) {

        return Integers.checkUnsignedLongToUnsignedInt(capacity);
    }

    long getCapacity();
}
