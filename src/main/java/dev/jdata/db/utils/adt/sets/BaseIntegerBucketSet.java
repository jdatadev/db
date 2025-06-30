package dev.jdata.db.utils.adt.sets;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

abstract class BaseIntegerBucketSet<T> extends BaseIntegerSet<T> {

    BaseIntegerBucketSet(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerBucketSet(BaseIntegerBucketSet<T> toCopy, BiConsumer<T, T> copyHashedContent) {
        super(toCopy, copyHashedContent);
    }
}
