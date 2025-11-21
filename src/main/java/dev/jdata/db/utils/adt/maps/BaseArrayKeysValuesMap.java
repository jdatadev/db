package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.marker.IEqualityTesterMarker;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseArrayKeysValuesMap<KEYS, VALUES, MAP extends BaseArrayKeysValuesMap<KEYS, VALUES, MAP>> extends BaseArrayKeysMap<KEYS, MAP> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY_KEYS_VALUES_MAP;

    @FunctionalInterface
    interface IntMapIndexValuesEqualityTester<T, P1, P2, DELEGATE, E extends Exception> extends IEqualityTesterMarker<P1, P2, E> {

        boolean areValuesEqual(T values1, int index1, P1 parameter1, T values2, int index2, P2 parameter2, DELEGATE delegate) throws E;
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValues<K, V, P1, P2, E extends Exception> {

        void each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2) throws E;
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValuesWithResult<K, V, P1, P2, DELEGATE, R, E extends Exception> {

        R each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2, DELEGATE delegate) throws E;
    }

    abstract void rehashKeysAndValues(KEYS hashArray, KEYS newHashArray, int newKeyMask, VALUES values, VALUES newValues);

    protected abstract void clearValues(VALUES values);

    abstract <KEYS_DST, VALUES_DST> int keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<KEYS, VALUES, KEYS_DST, VALUES_DST> keyValueAdder);

    private final IntFunction<VALUES> createValues;
    private VALUES values;

    BaseArrayKeysValuesMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed, IntFunction<VALUES> createValues) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createHashed", createHashed).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        final VALUES values = this.values = createValuesFromCapacity(this, createValues);

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseArrayKeysValuesMap(AllocationType allocationType, BaseArrayKeysValuesMap<KEYS, VALUES, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        this.createValues = null;
        this.values = toInitializeFrom.values;

        if (DEBUG) {

            exit();
        }
    }

    BaseArrayKeysValuesMap(AllocationType allocationType, BaseArrayKeysValuesMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        final IntFunction<VALUES> createValues = this.createValues = toCopy.createValues;

        final VALUES values = this.values = createValuesFromCapacity(toCopy, createValues);

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected void rehashWithKeyMask(KEYS hashArray, KEYS newHashArray, int newCapacity, int capacityExponentIncrease, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }


        final VALUES values = getValues();
        final VALUES newValues = recreateValuesAndReturnNew(newCapacity);

        rehashKeysAndValues(hashArray, newHashArray, newKeyMask, values, newValues);

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }
    }

    @Override
    protected void recreateElements() {

        super.recreateElements();

        recreateValues(getHashedCapacity());
    }

    @Override
    protected void resetToNull() {

        super.resetToNull();

        this.values = null;
    }

    private VALUES createValues(int capacity) {

        Checks.isIntCapacityAboveZero(capacity);

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity));
        }

        final VALUES result = createValuesFromCapacity(createValues, capacity);

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

        final VALUES result = this.values = createValuesFromCapacity(createValues, newOuterCapacity);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private VALUES recreateValuesAndReturnNew(int capacity) {

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity));
        }

        recreateValues(capacity);

        final VALUES result = values;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private void recreateValues(int capacity) {

        final VALUES values = this.values = createValues.apply(capacity);

        clearValues(values);
    }

    private static <VALUES> VALUES createValuesFromCapacity(BaseArrayKeysValuesMap<?, VALUES, ?> map, IntFunction<VALUES> createValues) {

        return createValuesFromCapacity(createValues, map.getHashedCapacity());
    }

    private static <VALUES> VALUES createValuesFromCapacity(IntFunction<VALUES> createValues, int capacity) {

        return createValues.apply(capacity);
    }
}
