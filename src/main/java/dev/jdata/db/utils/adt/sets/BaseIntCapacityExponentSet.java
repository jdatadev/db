package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentArrayHashed;

abstract class BaseIntCapacityExponentSet<T, U> extends BaseIntCapacityExponentArrayHashed<T, U, T> {

    BaseIntCapacityExponentSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed,
            Consumer<T> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntCapacityExponentSet(AllocationType allocationType, BaseIntCapacityExponentSet<T, U> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
