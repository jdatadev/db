package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentArrayHashed;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

abstract class BaseIntCapacityExponentMap<T> extends BaseIntCapacityExponentArrayHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_EXPONENT_ARRAY_HASHED;

    protected static final int NO_INDEX = HashArray.NO_INDEX;

    @FunctionalInterface
    protected interface IntMapIndexValuesEqualityTester<T, P1, P2, DELEGATE> {

        boolean areValuesEqual(T values1, int index1, P1 parameter1, T values2, int index2, P2 parameter2, DELEGATE delegate);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValues<K, V, P1, P2> {

        void each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValuesWithResult<K, V, P1, P2, DELEGATE, R> {

        R each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2, DELEGATE delegate);
    }

    BaseIntCapacityExponentMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityExponentMap(BaseIntCapacityExponentMap<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }
}
