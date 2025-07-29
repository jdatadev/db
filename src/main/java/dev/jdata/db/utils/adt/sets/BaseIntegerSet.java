package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseIntegerSet<T> extends BaseIntCapacityExponentSet<T> {

    BaseIntegerSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerSet(BaseIntegerSet<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);
    }
}
