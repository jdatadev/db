package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentHashed;

abstract class BaseExponentSet<T> extends BaseIntCapacityExponentHashed<T> {

    public BaseExponentSet(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseExponentSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }
}
