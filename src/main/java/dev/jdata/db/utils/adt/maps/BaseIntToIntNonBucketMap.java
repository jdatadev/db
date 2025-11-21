package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;

abstract class BaseIntToIntNonBucketMap<M extends BaseIntToIntNonBucketMap<M>> extends BaseIntArrayKeysNonBucketMap<int[], M> implements IIntToIntMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_INT_NON_BUCKET_MAP;

    BaseIntToIntNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToIntNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToIntNonBucketMap(AllocationType allocationType, BaseIntToIntNonBucketMap<M> toCopy) {
        super(allocationType, toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEachValue(P parameter, IIntForEachMapValue<P, E> forEach) throws E {

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
    public final <P, E extends Exception> void forEachKeyAndValue(P parameter, IIntToIntForEachMapKeyAndValue<P, E> forEach) throws E {

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
            IIntToIntForEachMapKeyAndValueWithResult<P1, P2, R, E> forEach) throws E {

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
    public final long keysAndValues(IIntAnyOrderAddable keysDst, IIntAnyOrderAddable valuesDst) {

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
    protected final void put(int[] values, int index, int[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(int[] valuesArray) {

    }

    @Deprecated // only cleas keys so move to base class for all similar?
    final void clearBaseIntToIntNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    final <P1, P2, E extends Exception> boolean equalsIntToIntNonBucketMap(P1 parameter1, BaseIntToIntNonBucketMap<?> other, P2 parameter2,
            IIntValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

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
