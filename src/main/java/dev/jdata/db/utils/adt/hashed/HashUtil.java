package dev.jdata.db.utils.adt.hashed;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

public class HashUtil {

    public static int computeRehashCapacityExponent(long numElements, float loadFactor) {

        Checks.isNotNegative(numElements);
        Checks.isLoadFactor(loadFactor);

        int capacityExponent;

        for (capacityExponent = 0; shouldRehashElements(numElements, CapacityExponents.computeIntCapacityFromExponent(capacityExponent), loadFactor); ++ capacityExponent) {

        }

        return capacityExponent;
    }

    static boolean shouldRehash(long numElements, long capacity, float loadFactor) {

        Checks.isNotNegative(numElements);
        Checks.isCapacity(capacity);
        Checks.isLessThanOrEqualTo(capacity, Double.MAX_VALUE);
        Checks.isLoadFactor(loadFactor);

        return shouldRehashElements(numElements, capacity, loadFactor);
    }

    static long computeRequiredCapacity(long numElements, long capacity, float loadFactor, int capacityExponentIncrease, boolean longCapacity) {

        Checks.isNotNegative(numElements);
        Checks.isCapacity(capacity);
        Checks.isLessThanOrEqualTo(capacity, Double.MAX_VALUE);
        Checks.isLoadFactor(loadFactor);
        Checks.isCapacityExponentIncrease(capacityExponentIncrease, longCapacity);
        Checks.isTrue(shouldRehash(numElements, capacity, loadFactor));

        long requiredCapacity = capacity != 0L ? capacity << capacityExponentIncrease : 1L << capacityExponentIncrease;

        while (shouldRehash(numElements, requiredCapacity, loadFactor)) {

            requiredCapacity <<= 1;
        }

        return requiredCapacity;
    }

    @Deprecated // utilize integer fraction for loadFactor to avoid floating point operation
    private static boolean shouldRehashElements(long numElements, long capacity, float loadFactor) {

        final double load = numElements / (double)capacity;

        return load > loadFactor;
    }
}
