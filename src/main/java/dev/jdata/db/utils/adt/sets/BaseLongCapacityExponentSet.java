package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongCapacityExponentSet<T extends IMutableLargeArrayMarker> extends BaseLongCapacityExponentArrayHashed<T> {

    BaseLongCapacityExponentSet(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseLongCapacityExponentSet(AllocationType allocationType, BaseLongCapacityExponentArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
