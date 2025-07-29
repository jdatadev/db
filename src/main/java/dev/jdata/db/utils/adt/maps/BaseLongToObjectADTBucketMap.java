package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectBucketMapMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectValues;

abstract class BaseLongToObjectADTBucketMap<T, MAP extends BaseLongToObjectADTBucketMap<T, MAP>>

        extends BaseLongToObjectBucketMap<T, LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T>, LongToObjectValues<MAP, T>, MAP> {

    static final class LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>

            extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedList<

                            INSTANCE,
                            T,
                            LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>,
                            LongToObjectValues<INSTANCE, T>> {

        LongToObjectBucketMapMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, innerCapacity, c -> new LongToObjectValues<>(c, createOuterArray, createArray));
        }
    }

    static final class LongToObjectValues<INSTANCE, T>

            extends BaseLongToObjectValues<INSTANCE, T, LongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T>, LongToObjectValues<INSTANCE, T>> {

        LongToObjectValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, createOuterArray, createArray);
        }
    }

    private final IntFunction<T[][]> createOuterArray;
    private final IntFunction<T[]> createArray;

    BaseLongToObjectADTBucketMap(int initialCapacityExponent, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(o, i, createOuterArray, createArray));

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.createArray = Objects.requireNonNull(createArray);
    }

    BaseLongToObjectADTBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(o, i, createOuterArray, createArray));

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.createArray = Objects.requireNonNull(createArray);
    }

    @Override
    final LongToObjectBucketMapMultiHeadSinglyLinkedList<MAP, T> createBuckets(int outerInitialCapacity, int bucketsInnerCapacity) {

        return new LongToObjectBucketMapMultiHeadSinglyLinkedList<>(outerInitialCapacity, bucketsInnerCapacity, createOuterArray, createArray);
    }
}
