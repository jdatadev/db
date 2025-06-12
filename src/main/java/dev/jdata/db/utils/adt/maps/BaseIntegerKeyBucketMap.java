package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.lists.BaseLargeList.BaseValuesFactory;
import dev.jdata.db.utils.adt.lists.BaseLargeSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseValues;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntegerKeyBucketMap<
                KEYS,
                LIST_T,
                LIST extends BaseLargeSinglyLinkedList<MAP, LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>,
                MAP extends BaseIntegerKeyBucketMap<KEYS, LIST_T, LIST, VALUES, MAP>>

        extends BaseIntCapacityExponentMap<KEYS> {

    abstract LIST createBuckets(int outerInitialCapacity, int bucketsInnerCapacity);

    private final int bucketsInnerCapacity;

    private LIST buckets;
    private VALUES values;

    BaseIntegerKeyBucketMap(MAP toCopy, BiConsumer<KEYS, KEYS> copyHashedContent, Function<VALUES, VALUES> copyValues) {
        super(toCopy, copyHashedContent);

        final BaseIntegerKeyBucketMap<?, ?, ?, ?, ?> other = toCopy;

        this.bucketsInnerCapacity = other.bucketsInnerCapacity;

        this.values = copyValues.apply(toCopy.getValues());
    }

    BaseIntegerKeyBucketMap(int initialCapacityExponent, float loadFactor, int bucketsInnerCapacityExponent, IntFunction<KEYS> createHashed, Consumer<KEYS> clearHashed,
            BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<LIST_T, LIST, VALUES> valuesFactory) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, bucketsInnerCapacityExponent, createHashed, clearHashed, createBuckets, valuesFactory);
    }

    BaseIntegerKeyBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent, IntFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed, BiIntToObjectFunction<LIST> createBuckets, BaseValuesFactory<LIST_T, LIST, VALUES> valuesFactory) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        Checks.isCapacityExponent(bucketsInnerCapacityExponent);

        final int bucketsInnerCapacity = this.bucketsInnerCapacity = CapacityExponents.computeCapacity(bucketsInnerCapacityExponent);

        this.buckets = createBuckets.apply(BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);

        this.values = createValues(valuesFactory);
    }

    final int getBucketsInnerCapacity() {
        return bucketsInnerCapacity;
    }

    final LIST getBuckets() {
        return buckets;
    }

    final void setBuckets(LIST buckets) {

        this.buckets = Objects.requireNonNull(buckets);
    }

    final VALUES getValues() {
        return values;
    }

    final void clearMap() {

        clearHashed();

        buckets.clear();
    }

    private VALUES createValues(BaseValuesFactory<LIST_T, LIST, VALUES> valuesFactory) {

        return valuesFactory.create(1);
    }
}
