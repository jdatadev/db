package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntToLongNonBucketMap<M extends IBaseIntToLongMapCommon<M>> extends BaseIntArrayNonBucketMap<long[]> implements IBaseIntToLongMapCommon<M> {

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
    public final <P> void forEachValue(P parameter, IForEachValue<P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        forEachKeyAndValue(parameter, forEach, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(values[valueIndex], p1));

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        forEachKeyAndValue(parameter, forEach, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<P, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        final R result = forEachKeyAndValueWithResult(defaultResult, parameter, forEach, delegate,
                (keys, keyIndex, values, valueIndex, p1, p2, d) -> p2.each(keys[keyIndex], values[valueIndex], p1, d));

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        return result;
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

    final <P1, P2, DELEGATE> boolean equalsIntToLongNonBucketMap(P1 parameter1, BaseIntToLongNonBucketMap<M> other, P2 parameter2,
            ILongValueMapEqualityTester<P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("equalityTester", equalityTester));
        }

        final boolean result = equalsIntKeyNonBucketMapWithIndex(parameter1, other, parameter2, equalityTester,
                (v1, i1, p1, v2, i2, p2, d) -> d.equals(v1[i1], p1, v2[i2], p2));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
