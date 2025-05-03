package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntegerBucketSet<T extends LargeExponentArray> extends BaseLargeIntegerSet<T> {

    BaseLargeIntegerBucketSet(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }
}
