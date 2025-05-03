package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntToLongNonBucketMap extends BaseIntArrayNonBucketMap<long[]> implements IIntToLongMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_LONG_NON_BUCKET_MAP;

    BaseIntToLongNonBucketMap(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    BaseIntToLongNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new);
    }

    BaseIntToLongNonBucketMap(BaseIntToLongNonBucketMap toCopy) {
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
    public final void keysAndValues(int[] keysDst, long[] valuesDst) {

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
    protected final void put(long[] values, int index, long[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(long[] valuesArray) {

    }
}
