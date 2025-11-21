package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

abstract class BaseIntegerSet<T, U> extends BaseIntCapacityExponentSet<T, U> {

    BaseIntegerSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntegerSet(AllocationType allocationType, BaseIntegerSet<T, U> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
