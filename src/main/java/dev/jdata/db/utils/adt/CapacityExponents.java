package dev.jdata.db.utils.adt;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public class CapacityExponents {

    public static final int DEFAULT_INNER_CAPACITY_EXPONENT = 10;

    public static int computeIntCapacityFromExponent(int capacityExponent) {

        Checks.isIntCapacityExponent(capacityExponent);

        return 1 << capacityExponent;
    }

    public static long computeLongCapacityFromExponent(int capacityExponent) {

        Checks.isLongCapacityExponent(capacityExponent);

        return 1L << capacityExponent;
    }

    public static int computeIntCapacityExponent(int numElements) {

        return computeCapacityExponent(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponent(long numElements) {

        return computeCapacityExponent(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponent(IOnlyElementsView elements) {

        Objects.requireNonNull(elements);

        return computeCapacityExponent(elements.getNumElements(), Integer.SIZE);
    }

    public static int computeLongCapacityExponent(long numElements) {

        return computeCapacityExponent(numElements, Long.SIZE);
    }

    public static int computeIntCapacityExponentExact(int numElements) {

        return computeCapacityExponentExact(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponentExact(long numElements) {

        return computeCapacityExponentExact(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponentExact(ICapacity capacity) {

        Objects.requireNonNull(capacity);

        return computeCapacityExponentExact(capacity.getCapacity(), Integer.SIZE);
    }

    public static int computeLongCapacityExponentExact(long numElements) {

        return computeCapacityExponentExact(numElements, Long.SIZE);
    }

    public static int computeArrayOuterCapacity(int capacity, int innerCapacityExponent) {

        return computeArrayOuterCapacity((long)capacity, innerCapacityExponent);
    }

    public static int computeArrayOuterCapacity(long capacity, int innerCapacityExponent) {

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

        Checks.isIntOrLongNumElements(numElements);

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

    public static int intCapacityExponent(long capacityExponent) {

        Checks.isIntCapacityExponent(capacityExponent);

        return (int)capacityExponent;
    }
}
