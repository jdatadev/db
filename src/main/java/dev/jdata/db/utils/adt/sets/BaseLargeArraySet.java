package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeArraySet<T extends LargeExponentArray<?, ?>> extends BaseLongCapacityExponentArrayHashed<T> {

    BaseLargeArraySet(int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(initialOuterCapacity, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }
}
