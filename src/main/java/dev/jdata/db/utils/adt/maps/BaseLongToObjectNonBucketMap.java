package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongToObjectNonBucketMap<T> extends BaseLongArrayNonBucketMap<T[]> implements ILongToObjectMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_NON_BUCKET_MAP;

    BaseLongToObjectNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongToObjectNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongToObjectNonBucketMap(BaseLongToObjectNonBucketMap<T> toCopy) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));
    }

    @Override
    public final <P> void forEachValue(P parameter, ForEachValue<T, P> forEachValue) {

        Objects.requireNonNull(forEachValue);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachValue", forEachValue));
        }

        forEachKeyAndValue(parameter, forEachValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(values[valueIndex], p1));

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEachValue", forEachValue));
        }
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<T, P> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }
    }

    @Override
    public final void keysAndValues(long[] keysDst, T[] valuesDst) {

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
    protected final void put(T[] values, int index, T[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(T[] values) {

        Arrays.fill(values, null);
    }

    final void clearBaseLongToObjectNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 100));

        sb.append(getClass().getSimpleName()).append(" {");

        final int prefixLength = sb.length();

        forEachKeyAndValue(sb, (k, v, b) -> {

            if (b.length() > prefixLength) {

                b.append(',');
            }

            b.append(k).append('=').append(v);
        });

        sb.append('}');

        return sb.toString();
    }
}
