package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;

abstract class BaseLongArrayMap<T> extends BaseLongMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_ARRAY_MAP;

//    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_MAP;

    BaseLongArrayMap(int initialCapacityExponent, IntFunction<T> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValuesArray);
    }

    BaseLongArrayMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }
}
