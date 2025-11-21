package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseArrayKeysValuesLargeMap<KEYS extends IMutableLargeArrayMarker, VALUES, MAP extends BaseArrayKeysValuesLargeMap<KEYS, VALUES, MAP>>

        extends BaseLargeArrayKeysLargeMap<KEYS, MAP> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY_KEYS_VALUES_LARGE_MAP;

    @FunctionalInterface
    interface LongMapIndexValuesEqualityTester<T, P1, P2, DELEGATE> {

        boolean areValuesEqual(T values1, long index1, P1 parameter1, T values2, long index2, P2 parameter2, DELEGATE delegate);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValuesWithResult<K, V, P1, P2, DELEGATE, R> {

        R each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2, DELEGATE delegate);
    }

    protected abstract int getOuterCapacity();
    protected abstract int getInnerCapacity();

    protected abstract void rehashKeysAndValues(KEYS hashArray, KEYS newHashArray, long newKeyMask, VALUES values, VALUES newValues);

    protected abstract void clearValues(VALUES values);

    abstract <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            ILongCapacityMapIndexKeyValueAdder<KEYS, VALUES, KEYS_DST, VALUES_DST> keyValueAdder);

    private final BiIntToObjectFunction<VALUES> createValues;
    private VALUES values;

    BaseArrayKeysValuesLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValuesFromCapacity(this, createValues);

        if (DEBUG) {

            exit();
        }
    }

    BaseArrayKeysValuesLargeMap(AllocationType allocationType, BaseArrayKeysValuesLargeMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        final BiIntToObjectFunction<VALUES> createValues = this.createValues = toCopy.createValues;

        final VALUES values = this.values = createValuesFromCapacity(toCopy, createValues);

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final void rehashWithKeyMask(KEYS hashArray, KEYS newHashArray, long newCapacity, int capacityExponentIncrease, int newCapacityExponent,
            long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final VALUES values = getValues();
        final VALUES newValues = recreateValuesAndReturnNew(newCapacity);

        rehashKeysAndValues(hashArray, newHashArray, newKeyMask, values, newValues);

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }
    }

    private VALUES recreateValuesAndReturnNew(long capacity) {

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity));
        }

        recreateValues(capacity);

        final VALUES result = getValues();

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private void recreateValues(long capacity) {

        final VALUES values = this.values = createValuesFromCapacity(this, createValues);

        clearValues(values);
    }

    private VALUES createValues(int outerCapacity, int innerCapacityExponent) {

        Checks.isOuterCapacityAboveZero(outerCapacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("outerCapacity", outerCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final VALUES result = createValuesFromCapacity(this, createValues, outerCapacity);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final VALUES getValues() {
        return values;
    }

    private void setValues(VALUES values) {

        this.values = Objects.requireNonNull(values);
    }

    private VALUES reallocateValues(int newOuterCapacity) {

        Checks.isOuterCapacityAboveZero(newOuterCapacity);

        if (DEBUG) {

            enter(b -> b.add("newOuterCapacity", newOuterCapacity));
        }

        final VALUES result = this.values = createValuesFromCapacity(this, createValues, newOuterCapacity);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private static <VALUES> VALUES createValuesFromCapacity(BaseArrayKeysValuesLargeMap<?, VALUES, ?> map, BiIntToObjectFunction<VALUES> createValues) {

        return createValuesFromCapacity(map, createValues, map.getOuterCapacity());
    }

    private static <VALUES> VALUES createValuesFromCapacity(BaseArrayKeysValuesLargeMap<?, VALUES, ?> map, BiIntToObjectFunction<VALUES> createValues, int outerCapacity) {

// fix
//        return createValues.apply(outerCapacity, map.getInnerCapacityExponent());
        throw new UnsupportedOperationException();
    }
}
