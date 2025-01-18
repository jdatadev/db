package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseExponentHashed;

abstract class BaseExponentMap<T> extends BaseExponentHashed<T> {

    BaseExponentMap(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseExponentMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }
}
