package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.maps.IToObjectMapGetters.ForEachValue;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntToObjectNonBucketMap<T> extends BaseIntArrayNonBucketMap<T[]> implements IIntToObjectMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_OBJECT_NON_BUCKET_MAP;

    BaseIntToObjectNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseIntToObjectNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createMap", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectNonBucketMap(BaseIntToObjectNonBucketMap<T> toCopy) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));
    }

    @Override
    public final <P> void forEachValue(P parameter, ForEachValue<T, P> forEachValue) {

        Objects.requireNonNull(forEachValue);

        forEachKeyAndValue(parameter, forEachValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(values[valueIndex], p1));
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<T, P> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    @Override
    public final void keysAndValues(int[] keysDst, T[] valuesDst) {

        final long numElements = getNumElements();

        Checks.areEqual(keysDst.length, numElements);
        Checks.areEqual(valuesDst.length, numElements);

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);
    }

    @Override
    protected final void put(T[] values, int index, T[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    @Override
    protected final void clearValues(T[] values) {

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
