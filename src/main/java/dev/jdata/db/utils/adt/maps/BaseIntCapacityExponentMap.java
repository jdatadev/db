package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentArrayHashed;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.marker.IEqualityTesterMarker;

abstract class BaseIntCapacityExponentMap<KEYS, VALUES> extends BaseIntCapacityExponentArrayHashed<KEYS> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_EXPONENT_ARRAY_HASHED;

    protected static final int NO_INDEX = HashArray.NO_INDEX;

    abstract <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<KEYS, KEYS_DST, VALUES, VALUES_DST> keyValueAdder);

    @FunctionalInterface
    protected interface IntMapIndexValuesEqualityTester<T, P1, P2, DELEGATE, E extends Exception> extends IEqualityTesterMarker<P1, P2, E> {

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

    BaseIntCapacityExponentMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createHashed, Consumer<KEYS> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityExponentMap(BaseIntCapacityExponentMap<KEYS, VALUES> toCopy, Function<KEYS, KEYS> copyHashed) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }
}
