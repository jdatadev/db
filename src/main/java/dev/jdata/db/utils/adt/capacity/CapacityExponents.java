package dev.jdata.db.utils.adt.capacity;

import java.util.Objects;

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

    public static int computeIntCapacityExponentForAtOrAboveZero(int numElements) {

        return computeCapacityExponentForAtOrAboveZero(numElements, Integer.SIZE);
    }

    private static int computeIntCapacityExponentForAboveZero(int numElements) {

        return computeCapacityExponentForAboveZero(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponentForAboveZero(long numElements) {

        return computeCapacityExponentForAboveZero(numElements, Integer.SIZE);
    }

    private static int computeIntCapacityExponentForAboveZero(IOnlyElementsView elements) {

        Objects.requireNonNull(elements);

        return computeCapacityExponentForAboveZero(elements.getNumElements(), Integer.SIZE);
    }

    @Deprecated // currently not in use
    static int computeLongCapacityExponentForAtOrAboveZero(long numElements) {

        return computeCapacityExponentForAtOrAboveZero(numElements, Long.SIZE);
    }

    public static int computeLongCapacityExponentForAboveZero(long numElements) {

        return computeCapacityExponentForAboveZero(numElements, Long.SIZE);
    }

    private static int computeIntCapacityExponentExactForAboveZero(int numElements) {

        return computeCapacityExponentExactForAboveZero(numElements, Integer.SIZE);
    }

    public static int computeIntCapacityExponentExactForAboveZero(long numElements) {

        return computeCapacityExponentExactForAboveZero(numElements, Integer.SIZE);
    }

    private static int computeIntCapacityExponentExactForAboveZero(ICapacity capacity) {

        Objects.requireNonNull(capacity);

        return computeCapacityExponentExactForAboveZero(capacity.getCapacity(), Integer.SIZE);
    }

    public static int computeLongCapacityExponentExactForAboveZero(long numElements) {

        return computeCapacityExponentExactForAboveZero(numElements, Long.SIZE);
    }

    public static int computeArrayOuterCapacity(int capacity, int innerCapacityExponent) {

        return computeArrayOuterCapacity((long)capacity, innerCapacityExponent);
    }

    public static int computeArrayOuterCapacity(long capacity, int innerCapacityExponent) {

        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        return Capacity.computeArrayOuterCapacity(capacity, innerCapacity);
    }

    private static int computeCapacityExponentExactForAboveZero(long numElements, int typeNumBits) {

        final int capacityExponent = computeCapacityExponentForAboveZero(numElements, typeNumBits);

        if (numElements != computeLongCapacityFromExponent(capacityExponent)) {

            throw new IllegalArgumentException();
        }

        return capacityExponent;
    }

    private static int computeCapacityExponentForAtOrAboveZero(long numElements, int typeNumBits) {

        Checks.isIntOrLongNumElements(numElements);

        return numElements == 0L ? 0 : computeCapacityExponentForAboveZero(numElements, typeNumBits);
    }

    private static int computeCapacityExponentForAboveZero(long numElements, int typeNumBits) {

        Checks.isIntOrLongNumElements(numElements);
        Checks.isAboveZero(numElements);

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
