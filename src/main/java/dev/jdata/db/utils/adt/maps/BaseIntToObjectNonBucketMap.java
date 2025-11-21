package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.maps.Maps.IIntForEachAppend;

abstract class BaseIntToObjectNonBucketMap<V, M extends BaseIntToObjectNonBucketMap<V, M>> extends BaseIntArrayKeysNonBucketMap<V[], M> implements IIntToObjectMapCommon<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_OBJECT_NON_BUCKET_MAP;

    BaseIntToObjectNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectNonBucketMap(AllocationType allocationType, BaseIntToObjectNonBucketMap<V, M> toCopy) {
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
    public final <P, E extends Exception> void forEachKeyAndValue(P parameter, IIntToObjectForEachMapKeyAndValue<V, P, E> forEach) throws E {

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
    public final <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            IIntToObjectForEachMapKeyAndValueWithResult<V, P1, P2, R, E> forEach) throws E {

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
    public final long keysAndValues(IIntAnyOrderAddable keysDst, IObjectAnyOrderAddable<V> valuesDst) {

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

    final void clearBaseIntToObjectNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    final <P1, P2, DELEGATE, E extends Exception> boolean equalsIntToObjectNonBucketMap(P1 parameter1, BaseIntToObjectNonBucketMap<V, ?> other, P2 parameter2,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

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

    @Override
    public final String toString() {

        return Maps.intToObjectMapToString(getClass().getSimpleName(), getNumElements(), this,
                (StringBuilder b, BaseIntToObjectNonBucketMap<V, M> i, IIntForEachAppend<V, BaseIntToObjectNonBucketMap<V, M>> f)
                        -> i.forEachKeyAndValue(b, (key, value, stringBuilder) -> f.each(key, value, stringBuilder, null)));
    }
}
