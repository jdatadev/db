package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.checks.Checks;

public class CapacityExponents {

    public static int computeCapacity(int capacityExponent) {

        Checks.isCapacityExponent(capacityExponent);

        return 1 << capacityExponent;
    }
}
