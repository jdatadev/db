package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntegerKeyMap<T extends LargeExponentArray<?, ?>, U> extends BaseLargeArrayKeysValuesPuttableMap<T, U> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_INTEGER_KEY_MAP;

    @FunctionalInterface
    protected interface IValueSetter<T, U> {

        void setValue(T src, long srcIndex, U dst, long dstIndex);
    }

    BaseLargeIntegerKeyMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed,
            Consumer<T> clearHashed, BiIntToObjectFunction<U> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeIntegerKeyMap(BaseLargeIntegerKeyMap<T, U> toCopy, Function<T, T> copyHashed, BiConsumer<U, U> copyValuesContent) {
        super(toCopy, copyHashed, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }
}
