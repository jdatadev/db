package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public class CapacityExponents {

    public static int computeIntCapacityFromExponent(int capacityExponent) {

        Checks.isIntCapacityExponent(capacityExponent);

        return 1 << capacityExponent;
    }

    public static long computeLongCapacityFromExponent(int capacityExponent) {

        Checks.isLongCapacityExponent(capacityExponent);

        return 1L << capacityExponent;
    }

    public static int computeCapacityExponent(int numElements) {

        return computeCapacityExponent(numElements, Integer.SIZE);
    }

    public static int computeCapacityExponent(long numElements) {

        return computeCapacityExponent(numElements, Long.SIZE);
    }

    public static int computeCapacityExponentExact(int numElements) {

        return computeCapacityExponentExact(numElements, Integer.SIZE);
    }

    public static int computeCapacityExponentExact(long numElements) {

        return computeCapacityExponentExact(numElements, Long.SIZE);
    }

    public static int computeArrayOuterCapacity(int capacity, int innerCapacityExponent) {

        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        return Capacity.computeArrayOuterCapacity(capacity, innerCapacity);
    }

    private static int computeCapacityExponentExact(long numElements, int typeNumBits) {

        final int capacityExponent = computeCapacityExponent(numElements, typeNumBits);

        if (numElements != computeLongCapacityFromExponent(capacityExponent)) {

            throw new IllegalArgumentException();
        }

        return capacityExponent;
    }

    private static int computeCapacityExponent(long numElements, int typeNumBits) {

        Checks.isNumElements(numElements);

        final int highestBit = BitsUtil.getIndexOfHighestSetBit(numElements);

        if (highestBit >= typeNumBits - 1) {

            throw new IllegalArgumentException();
        }

        return numElements == 1L << highestBit ? highestBit : highestBit + 1;
    }

    public static int makeIntKeyMask(int capacityExponent) {

        Checks.isIntCapacityExponent(capacityExponent);

        return (1 << capacityExponent) - 1;
    }

    public static long makeLongKeyMask(int capacityExponent) {

        Checks.isIntCapacityExponent(capacityExponent);

        return (1L << capacityExponent) - 1;
    }
}
