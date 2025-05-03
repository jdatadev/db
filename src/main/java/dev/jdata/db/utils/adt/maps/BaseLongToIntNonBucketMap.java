package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongToIntNonBucketMap extends BaseLongArrayNonBucketMap<int[]> implements ILongToIntMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_INT_NON_BUCKET_MAP;

    BaseLongToIntNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent, int[]::new);
    }

    BaseLongToIntNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new);
    }

    BaseLongToIntNonBucketMap(BaseLongToIntNonBucketMap toCopy) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }
    }

    @Override
    public final void keysAndValues(long[] keysDst, int[] valuesDst) {

        final long numElements = getNumElements();

        Checks.isGreaterThanOrEqualTo(keysDst.length, numElements);
        Checks.isGreaterThanOrEqualTo(valuesDst.length, numElements);
        Checks.areEqual(keysDst.length, valuesDst.length);

        if (DEBUG) {

            enter();
        }

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);

        if (DEBUG) {

            exit(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }
    }

    @Override
    protected final void put(int[] values, int index, int[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(int[] valuesArray) {

    }

    final void clearBaseLongToIntNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
