package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseObjectSet<T, U> extends BaseIntCapacityExponentSet<T[], U> {

    private final IntFunction<T[]> createHashed;

    BaseObjectSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createHashed,
            Consumer<T[]> clearHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        this.createHashed = Objects.requireNonNull(createHashed);
    }

    BaseObjectSet(AllocationType allocationType, BaseObjectSet<T, U> toCopy, Function<T[], T[]> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        this.createHashed = toCopy.createHashed;
    }

    final T[] createHashed(int capacity) {

        Checks.isIntCapacity(capacity);

        return createHashed.apply(capacity);
    }
}
