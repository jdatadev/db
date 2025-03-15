package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.IntFunction;

abstract class BaseIntegerSet<T> extends BaseExponentSet<T> {

    BaseIntegerSet(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }
}
