package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.adt.lists.LargeLists;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntegerBucketSet<T extends LargeExponentArray<?, ?>> extends BaseLargeIntegerSet<T> {

    static final long NO_LONG_NODE = LargeLists.NO_LONG_NODE;

    BaseLargeIntegerBucketSet(int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(initialOuterCapacity, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }
}
