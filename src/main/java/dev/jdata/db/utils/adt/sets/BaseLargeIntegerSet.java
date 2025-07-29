package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntegerSet<T extends LargeExponentArray<?, ?>> extends BaseLargeArraySet<T> {

    BaseLargeIntegerSet(int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(initialOuterCapacity, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }
}
