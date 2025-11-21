package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

@Deprecated // necessary?
abstract class BaseLargeArrayKeysLargeMap<KEYS extends IMutableLargeArrayMarker, MAP extends BaseLargeArrayKeysLargeMap<KEYS, MAP>>

        extends BaseLongCapacityExponentMap<KEYS, MAP> {

    BaseLargeArrayKeysLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseLargeArrayKeysLargeMap(AllocationType allocationType, BaseLargeArrayKeysLargeMap<KEYS, ?> toCopy, Function<KEYS, KEYS> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
