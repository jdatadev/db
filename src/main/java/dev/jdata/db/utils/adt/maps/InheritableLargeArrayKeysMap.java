package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

public abstract class InheritableLargeArrayKeysMap<KEYS extends IMutableLargeArrayMarker> extends BaseLargeArrayKeysMap<KEYS> {

    protected InheritableLargeArrayKeysMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor, BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    protected InheritableLargeArrayKeysMap(AllocationType allocationType, BaseLargeArrayKeysMap<KEYS> toCopy, Function<KEYS, KEYS> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
