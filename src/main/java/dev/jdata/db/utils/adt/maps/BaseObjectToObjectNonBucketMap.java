package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.maps.Maps.IObjectForEachAppend;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseObjectToObjectNonBucketMap<K, V, M extends IBaseObjectToObjectMapCommon<K, V, M>>

        extends BaseObjectKeyNonBucketMap<K, V[]>
        implements IBaseObjectToObjectMapCommon<K, V, M> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_TO_OBJECT_NON_BUCKET_MAP;

    BaseObjectToObjectNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);
    }

    BaseObjectToObjectNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectToObjectNonBucketMap(BaseObjectToObjectNonBucketMap<K, V, M> toCopy) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));
    }

    @Override
    public final <P> void forEachValue(P parameter, IForEachValue<V, P> forEach) {

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
    public final <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<K, V, P> forEach) {

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
    public final <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<K, V, P, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        final R result = forEachKeyAndValueWithResultWithResult(defaultResult, parameter, delegate, forEach,
                (keys, keyIndex, values, valueIndex, p1, p2, f) -> f.each(keys[keyIndex], values[valueIndex], p1, p2));

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter", parameter).add("delegate", delegate).add("forEach", forEach));
        }

        return result;
    }

    @Override
    public final void keysAndValues(K[] keysDst, V[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    @Override
    protected final void put(V[] values, int index, V[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(V[] values) {

        Arrays.fill(values, null);
    }

    final void clearBaseObjectToObjectNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    final <P1, P2, DELEGATE> boolean equalsObjectToObjectNonBucketMap(P1 parameter1, BaseObjectToObjectNonBucketMap<K, V, M> other, P2 parameter2,
            IObjectValueMapEqualityTester<V, P1, P2> equalityTester) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("equalityTester", equalityTester));
        }

        final boolean result = equalsObjectKeyNonBucketMapWithIndex(parameter1, other, parameter2, equalityTester,
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
            final BaseObjectMaxDistanceNonBucketMap<K, V> other = (BaseObjectMaxDistanceNonBucketMap<K, V>)object;

            result = equalsObjectKeyNonBucketMapWithIndex(other, null, null, null, (v1, i1, p1, v2, i2, p2, d) -> Objects.equals(v1[i1], v2[i2]));
        }

        return result;
    }

    @Override
    public final String toString() {

        return Maps.objectToObjectMapToString(getClass().getSimpleName(), getNumElements(), this,
                (StringBuilder b, BaseObjectToObjectNonBucketMap<K, V, M> i, IObjectForEachAppend<K, V, BaseObjectToObjectNonBucketMap<K, V, M>> f)
                        -> i.forEachKeyAndValue(b, (k, v, forEachStringBuilder) -> f.each(k, v, b, i)));
    }
}
