package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeArrayKeysValuesPuttableMap<KEYS extends IMutableLargeArrayMarker, VALUES> extends BaseLargeArrayKeysValuesMap<KEYS, VALUES> {

    protected abstract void put(VALUES values, long index, VALUES newValues, long newIndex);
    protected abstract void clearValues(VALUES values);

    BaseLargeArrayKeysValuesPuttableMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseLargeArrayKeysValuesPuttableMap(BaseLargeArrayKeysValuesMap<KEYS, VALUES> toCopy, Function<KEYS, KEYS> copyHashed, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(toCopy, copyHashed, copyValuesContent);
    }
}
