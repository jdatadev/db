package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongCapacityExponentMap<KEYS extends IMutableLargeArrayMarker, MAP extends BaseLongCapacityExponentMap<KEYS, MAP>>

        extends BaseLongCapacityExponentArrayHashed<KEYS> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_CAPACITY_EXPONENT_MAP;

    static final long NO_INDEX = LargeHashArray.NO_INDEX;

    BaseLongCapacityExponentMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor, BiIntToObjectFunction<KEYS> createKeysArray, Consumer<KEYS> clearKeysArray) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createKeysArray, clearKeysArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("clearKeysArray", clearKeysArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongCapacityExponentMap(AllocationType allocationType, BaseLongCapacityExponentMap<KEYS, ?> toCopy, Function<KEYS, KEYS> copyKeys) {
        super(allocationType, toCopy, copyKeys);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyKeys", copyKeys));
        }

        if (DEBUG) {

            exit();
        }
    }
}
