package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseArrayKeysValuesPuttableMap<KEYS, VALUES, MAP extends BaseArrayKeysValuesPuttableMap<KEYS, VALUES, MAP>> extends BaseArrayKeysValuesMap<KEYS, VALUES, MAP> {

    protected abstract void putValue(VALUES values, int index, VALUES newValues, int newIndex);

    BaseArrayKeysValuesPuttableMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed, IntFunction<VALUES> createValues) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseArrayKeysValuesPuttableMap(AllocationType allocationType, BaseArrayKeysValuesPuttableMap<KEYS, VALUES, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);
    }

    BaseArrayKeysValuesPuttableMap(AllocationType allocationType, BaseArrayKeysValuesPuttableMap<KEYS, VALUES, ?> toCopy, Function<KEYS, KEYS> copyHashed,
        BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed, copyValuesContent);
    }
}
