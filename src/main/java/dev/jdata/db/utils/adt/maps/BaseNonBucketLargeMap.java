package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseNonBucketLargeMap<KEYS extends IMutableLargeArrayMarker, VALUES, MAP extends BaseNonBucketLargeMap<KEYS, VALUES, MAP>>

        extends BaseArrayKeysValuesPuttableLargeMap<KEYS, VALUES, MAP> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_NON_BUCKET_LARGE_MAP;

    BaseNonBucketLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("clearHashed", clearHashed).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseNonBucketLargeMap(AllocationType allocationType, BaseNonBucketLargeMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    final void clearNonBucketLargeMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
