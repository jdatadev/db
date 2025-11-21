package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.INodeListView;
import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectBucketMapMultiHeadSinglyLinkedNodeList;
import dev.jdata.db.utils.adt.maps.BaseLongToObjectADTBucketMap.LongToObjectValues;

abstract class BaseLongToObjectADTBucketMap<V, MAP extends BaseLongToObjectADTBucketMap<V, MAP>>

        extends BaseLongToObjectBucketMap<V, LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<MAP, V>, LongToObjectValues<MAP, V>, MAP> {

    static final class LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<INSTANCE, T>

            extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedNodeList<

                            INSTANCE,
                            T,
                            LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<INSTANCE, T>,
                            LongToObjectValues<INSTANCE, T>> {

        private final IntFunction<T[][]> createOuterArray;
        private final IntFunction<T[]> createArray;

        LongToObjectBucketMapMultiHeadSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity, IntFunction<T[][]> createOuterArray,
                IntFunction<T[]> createArray) {
            super(allocationType, initialOuterCapacity, innerCapacity, c -> new LongToObjectValues<>(c, createOuterArray, createArray));

            this.createOuterArray = Objects.requireNonNull(createOuterArray);
            this.createArray = Objects.requireNonNull(createArray);
        }

        @Override
        protected INodeListView createEmpty(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {

            checkCreateEmpty(allocationType, initialOuterCapacity, innerCapacity);

            return new LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<>(allocationType, initialOuterCapacity, innerCapacity, createOuterArray, createArray);
        }
    }

    static final class LongToObjectValues<INSTANCE, T> extends BaseLongToObjectValues<T> {

        LongToObjectValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
            super(initialOuterCapacity, createOuterArray, createArray);
        }
    }

    BaseLongToObjectADTBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            IntFunction<V[][]> createOuterArray, IntFunction<V[]> createArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent,
                (o, i) -> new LongToObjectBucketMapMultiHeadSinglyLinkedNodeList<>(AllocationType.HEAP, o, i, createOuterArray, createArray));
    }
}
