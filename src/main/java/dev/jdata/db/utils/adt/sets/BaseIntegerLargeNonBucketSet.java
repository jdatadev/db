package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntegerLargeNonBucketSet<T extends IMutableLargeArrayMarker> extends BaseLargeIntegerSet<T> {

    BaseIntegerLargeNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerLargeNonBucketSet(BaseLongCapacityExponentArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);
    }
}
