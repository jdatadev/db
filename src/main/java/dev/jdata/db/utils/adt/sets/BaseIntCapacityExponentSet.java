package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentArrayHashed;

abstract class BaseIntCapacityExponentSet<T> extends BaseIntCapacityExponentArrayHashed<T> {

    BaseIntCapacityExponentSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntCapacityExponentSet(BaseIntCapacityExponentSet<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);
    }
}
