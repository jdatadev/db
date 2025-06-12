package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppend;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongToObjectNonBucketMap<T, M extends IBaseLongToObjectMapCommon<T, M>>

        extends BaseLongArrayNonBucketMap<T[]>
        implements IBaseLongToObjectMapCommon<T, M> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_NON_BUCKET_MAP;

    BaseLongToObjectNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongToObjectNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseLongToObjectNonBucketMap(BaseLongToObjectNonBucketMap<T, M> toCopy) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));
    }

    @Override
    public final <P> void forEachValue(P parameter, IForEachValue<T, P> forEach) {

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
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<T, P> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        forEachKeyAndValue(parameter, forEach, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<T, P, DELEGATE, R> forEach) {

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

    final <P1, P2, DELEGATE> boolean equalsLongToObjectNonBucketMap(P1 parameter1, BaseLongToObjectNonBucketMap<T, M> other, P2 parameter2,
            IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("equalityTester", equalityTester));
        }

        final boolean result = equalsLongKeyNonBucketMapWithIndex(parameter1, other, parameter2, equalityTester,
                (v1, i1, p1, v2, i2, p2, d) -> d.equals(v1[i1], p1, v2[i2], p2));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<T, M> other = (BaseLongToObjectNonBucketMap<T, M>)object;

            result = equalsLongKeyNonBucketMapWithIndex(null, other, null, null, (v1, i1, p1, v2, i2, p2, d) -> Objects.equals(v1[i1], v2[i2]));
        }

        return result;
    }

    @Override
    public final String toString() {

        return Maps.longToObjectMapToString(getClass().getSimpleName(), getNumElements(), this,
                (StringBuilder b, BaseLongToObjectNonBucketMap<T, M> i, ILongForEachAppend<T, BaseLongToObjectNonBucketMap<T, M>> f)
                        -> i.forEachKeyAndValue(b, (key, value, stringBuilder) -> f.each(key, value, stringBuilder, null)));
    }
}
