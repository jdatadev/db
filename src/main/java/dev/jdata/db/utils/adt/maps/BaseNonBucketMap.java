package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseNonBucketMap<KEYS, VALUES, MAP extends BaseNonBucketMap<KEYS, VALUES, MAP>> extends BaseArrayKeysValuesPuttableMap<KEYS, VALUES, MAP> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_NON_BUCKET_MAP;

    BaseNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createKeysArray,
            Consumer<KEYS> clearKeysArray, IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, clearKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("clearKeysArray", clearKeysArray).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseNonBucketMap(AllocationType allocationType, BaseNonBucketMap<KEYS, VALUES, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseNonBucketMap(AllocationType allocationType, BaseNonBucketMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyKeys, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyKeys, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyKeys", copyKeys).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    final void clearBaseNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
