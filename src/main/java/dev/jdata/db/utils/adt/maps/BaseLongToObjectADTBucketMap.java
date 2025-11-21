package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectBucketMapMultiHeadSinglyLinkedNodeList;
import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectValues;

abstract class BaseLongToObjectADTBucketMap<T, MAP extends BaseLongToObjectADTBucketMap<T, MAP>>

        extends BaseLongToObjectBucketMap<T, LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<MAP, T>, LongToObjectValues<MAP, T>, MAP> {

    static final class LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<INSTANCE, T>

            extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList<

                            INSTANCE,
                            T,
                            LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<INSTANCE, T>,
                            LongToObjectValues<INSTANCE, T>> {

        LongToObjectBucketMapMultiHeadSinglyLinkedNodeList(int initialOuterCapacity, int innerCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, innerCapacity, c -> new LongToObjectValues<>(c, createOuterArray, createArray));
        }
    }

    static final class LongToObjectValues<INSTANCE, T> extends BaseLongToObjectValues<T> {

        LongToObjectValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, createOuterArray, createArray);
        }
    }

    private final IntFunction<T[][]> createOuterArray;
    private final IntFunction<T[]> createArray;

    BaseLongToObjectADTBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
        super(allocationType, initialCapacityExponent, (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<>(o, i, createOuterArray, createArray));

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.createArray = Objects.requireNonNull(createArray);
    }

    BaseLongToObjectADTBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor,
                (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<>(o, i, createOuterArray, createArray));

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.createArray = Objects.requireNonNull(createArray);
    }

    @Override
    final LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<MAP, T> createBuckets(int outerInitialCapacity, int bucketsInnerCapacity) {

        return new LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<>(outerInitialCapacity, bucketsInnerCapacity, createOuterArray, createArray);
    }
}
