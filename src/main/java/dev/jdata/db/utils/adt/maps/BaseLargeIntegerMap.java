package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

@Deprecated // currently not in use
abstract class BaseLargeIntegerMap<T extends LargeExponentArray, U> extends BaseLargeArrayMap<T, U> {

    @FunctionalInterface
    protected interface ValueSetter<T, U> {

        void setValue(T src, long srcIndex, U dst, long dstIndex);
    }

    BaseLargeIntegerMap(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed,
            BiIntToObjectFunction<U> createValues) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }
}
