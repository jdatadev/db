package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppend;

abstract class BaseLongToObjectNonBucketMap<V, M extends BaseLongToObjectNonBucketMap<V, M>> extends BaseLongArrayKeysNonBucketMap<V[], M> implements ILongToObjectMapCommon<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_NON_BUCKET_MAP;

    BaseLongToObjectNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToObjectNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToObjectNonBucketMap(AllocationType allocationType, BaseLongToObjectNonBucketMap<V, M> toCopy) {
        super(allocationType, toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEachValue(P parameter, IObjectForEachMapValue<V, P, E> forEach) throws E {

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
    public final <P, E extends Exception> void forEachKeyAndValue(P parameter, ILongToObjectForEachMapKeyAndValue<V, P, E> forEach) throws E {

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
    public final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ILongToObjectForEachMapKeyAndValue2<V, P1, P2, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        forEachKeyAndValueWithResult(null, parameter1, parameter2, forEach, (keys, keyIndex, values, valueIndex, p1, p2, d) -> {

            d.each(keys[keyIndex], values[valueIndex], p1, p2);

            return null;
        });

        if (DEBUG) {

            exit(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToObjectForEachMapKeyAndValueWithResult<V, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final R result = forEachKeyAndValueWithResult(defaultResult, parameter1, parameter2, forEach,
                (keys, keyIndex, values, valueIndex, p1, p2, d) -> d.each(keys[keyIndex], values[valueIndex], p1, p2));

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        return result;
    }

    @Override
    public final long keysAndValues(ILongAnyOrderAddable keysDst, IObjectAnyOrderAddable<V> valuesDst) {

        Objects.requireNonNull(keysDst);
        Objects.requireNonNull(valuesDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }

        final long result = keysAndValues(keysDst, valuesDst, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> {

            kDst.addInAnyOrder(kSrc[srcIndex]);
            vDst.addInAnyOrder(vSrc[srcIndex]);
        });

        if (DEBUG) {

            exit(result, b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }

        return result;
    }

    @Override
    protected final void put(V[] values, int index, V[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(V[] values) {

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

    final <P1, P2, DELEGATE, E extends Exception> boolean equalsLongToObjectNonBucketMap(P1 parameter1, BaseLongToObjectNonBucketMap<V, ?> other, P2 parameter2,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

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
            final BaseLongToObjectNonBucketMap<V, ?> other = (BaseLongToObjectNonBucketMap<V, ?>)object;

            result = equalsLongKeyNonBucketMapWithIndex(null, other, null, null, (v1, i1, p1, v2, i2, p2, d) -> Objects.equals(v1[i1], v2[i2]));
        }

        return result;
    }

    @Override
    public final String toString() {

        return Maps.longToObjectMapToString(getClass().getSimpleName(), getNumElements(), this,
                (StringBuilder b, BaseLongToObjectNonBucketMap<V, M> i, ILongForEachAppend<V, BaseLongToObjectNonBucketMap<V, M>> f)
                        -> i.forEachKeyAndValue(b, (key, value, stringBuilder) -> f.each(key, value, stringBuilder, null)));
    }
}
