package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseIntCapacityExponentHashed;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

public abstract class BaseIntCapacityExponentMap<T> extends BaseIntCapacityExponentHashed<T> {

    protected static final int NO_INDEX = HashArray.NO_INDEX;

    @FunctionalInterface
    protected interface MapIndexValueSetter<T, U> {

        void setValue(T src, int srcIndex, U dst, int dstIndex);
    }

    @FunctionalInterface
    protected interface MapIndexValuesEqualityTester<T, P1, P2, DELEGATE> {

        boolean areValuesEqual(T values1, int index1, P1 parameter1, T values2, int index2, P2 parameter2, DELEGATE delegate);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValues<K, V, P1, P2> {

        void each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValuesWithResult<K, V, P1, P2, DELEGATE, R> {

        R each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2, DELEGATE delegate);
    }

    BaseIntCapacityExponentMap(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseIntCapacityExponentMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }

    BaseIntCapacityExponentMap(BaseIntCapacityExponentMap<T> toCopy, BiConsumer<T, T> copyHashedContent) {
        super(toCopy, copyHashedContent);
    }
}
