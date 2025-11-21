package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

@Deprecated // neccesary?
abstract class BaseArrayKeysMap<KEYS, MAP extends BaseArrayKeysMap<KEYS, MAP>> extends BaseIntCapacityExponentMap<KEYS, MAP> {

    BaseArrayKeysMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseArrayKeysMap(AllocationType allocationType, BaseArrayKeysMap<KEYS, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);
    }

    BaseArrayKeysMap(AllocationType allocationType, BaseArrayKeysMap<KEYS, ?> toCopy, Function<KEYS, KEYS> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
