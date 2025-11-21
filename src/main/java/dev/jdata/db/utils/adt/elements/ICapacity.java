package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.scalars.Integers;

public interface ICapacity {

    public static int intCapacityRenamed(long capacity) {

        return Integers.checkUnsignedLongToUnsignedInt(capacity);
    }

    public static int intCapacity(ICapacity capacity) {

        Objects.requireNonNull(capacity);

        return intCapacityRenamed(capacity.getCapacity());
    }

    long getCapacity();
}
