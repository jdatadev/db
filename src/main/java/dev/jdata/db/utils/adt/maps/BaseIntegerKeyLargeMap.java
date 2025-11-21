package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntegerKeyLargeMap<KEYS extends IMutableLargeArrayMarker, VALUES> extends BaseLargeArrayKeysValuesPuttableMap<KEYS, VALUES> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_INTEGER_KEY_MAP;

    @FunctionalInterface
    protected interface IValueSetter<T, U> {

        void setValue(T src, long srcIndex, U dst, long dstIndex);
    }

    BaseIntegerKeyLargeMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntegerKeyLargeMap(BaseIntegerKeyLargeMap<KEYS, VALUES> toCopy, Function<KEYS, KEYS> copyHashed, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(toCopy, copyHashed, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }
}
