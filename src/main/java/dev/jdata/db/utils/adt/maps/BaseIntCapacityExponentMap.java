package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentArrayHashed;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

abstract class BaseIntCapacityExponentMap<KEYS, MAP extends BaseIntCapacityExponentMap<KEYS, MAP>>

        extends BaseIntCapacityExponentArrayHashed<KEYS, Void, MAP, Void> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_EXPONENT_MAP;

    protected static final int NO_INDEX = HashArray.NO_INDEX;

    BaseIntCapacityExponentMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createKeysArray,
            Consumer<KEYS> clearKeysArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, clearKeysArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("clearKeysArray", clearKeysArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityExponentMap(AllocationType allocationType, BaseIntCapacityExponentMap<KEYS, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityExponentMap(AllocationType allocationType, BaseIntCapacityExponentMap<KEYS, ?> toCopy, Function<KEYS, KEYS> copyKeys) {
        super(allocationType, toCopy, copyKeys);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyKeys", copyKeys));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final Void copyValues(Void values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }

    final int getElementsCapacity() {

        return getHashedCapacity();
    }
}
