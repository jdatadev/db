package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseExponentHashed;

abstract class BaseIntegerSet<T> extends BaseExponentHashed<T> {

    BaseIntegerSet(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed) {
        super(initialCapacityExponent, loadFactor, createHashed);
    }

    BaseIntegerSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed);
    }
}
