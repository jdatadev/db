package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeArrayKeysValuesPuttableMap<T extends LargeExponentArray<?, ?>, U> extends BaseLargeArrayKeysValuesMap<T, U> {

    protected abstract void put(U values, long index, U newValues, long newIndex);
    protected abstract void clearValues(U values);

    BaseLargeArrayKeysValuesPuttableMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed, BiIntToObjectFunction<U> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseLargeArrayKeysValuesPuttableMap(BaseLargeArrayKeysValuesMap<T, U> toCopy, Function<T, T> copyHashed, BiConsumer<U, U> copyValuesContent) {
        super(toCopy, copyHashed, copyValuesContent);
    }
}
