package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseArrayKeysValuesPuttableLargeMap<KEYS extends IMutableLargeArrayMarker, VALUES, MAP extends BaseArrayKeysValuesPuttableLargeMap<KEYS, VALUES, MAP>>

        extends BaseArrayKeysValuesLargeMap<KEYS, VALUES, MAP> {

    protected abstract void putValue(VALUES values, long index, VALUES newValues, long newIndex);

    BaseArrayKeysValuesPuttableLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor, BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseArrayKeysValuesPuttableLargeMap(AllocationType allocationType, BaseArrayKeysValuesPuttableLargeMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed, copyValuesContent);
    }
}
