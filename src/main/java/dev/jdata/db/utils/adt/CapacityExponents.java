package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public class CapacityExponents {

    public static int computeCapacity(int capacityExponent) {

        Checks.isCapacityExponent(capacityExponent);

        return 1 << capacityExponent;
    }

    public static int computeCapacityExponent(int numElements) {

        return computeCapacityExponent(numElements, Integer.SIZE);
    }

    public static int computeCapacityExponent(long numElements) {

        return computeCapacityExponent(numElements, Long.SIZE);
    }

    public static int computeArrayOuterCapacity(int initialCapacity, int innerCapacityExponent) {

        final int innerCapacity = CapacityExponents.computeCapacity(innerCapacityExponent);

        return Capacity.computeArrayOuterCapacity(initialCapacity, innerCapacity);
    }

    private static int computeCapacityExponent(long numElements, int typeNumBits) {

        Checks.isNumElements(numElements);

        final int highestBit = BitsUtil.getIndexOfHighestSetBit(numElements);

        if (highestBit >= typeNumBits - 1) {

            throw new IllegalArgumentException();
        }

        return highestBit + 1;
    }

    public static int makeIntKeyMask(int capacityExponent) {

        Checks.isCapacityExponent(capacityExponent);

        return (1 << capacityExponent) - 1;
    }

    public static long makeLongKeyMask(int capacityExponent) {

        Checks.isCapacityExponent(capacityExponent);

        return (1L << capacityExponent) - 1;
    }
}
