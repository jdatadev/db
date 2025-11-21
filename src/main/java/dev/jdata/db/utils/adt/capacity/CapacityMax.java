package dev.jdata.db.utils.adt.capacity;

import dev.jdata.db.utils.checks.Checks;

public enum CapacityMax {

    INT,
    LONG;

    public static long checkMinimumCapacityAboveZero(CapacityMax capacityMax, long minimumCapacity) {

        switch (capacityMax) {

        case INT:

            Checks.isIntMinimumCapacityAboveZero(minimumCapacity);
            break;

        case LONG:

            Checks.isLongMinimumCapacityAboveZero(minimumCapacity);
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return minimumCapacity;
    }
}
