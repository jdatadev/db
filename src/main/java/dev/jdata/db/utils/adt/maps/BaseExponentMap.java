package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.hashed.BaseExponentHashed;

public abstract class BaseExponentMap<T> extends BaseExponentHashed<T> {

    @FunctionalInterface
    protected interface ValueSetter<T, U> {

        void setValue(T src, int srcIndex, U dst, int dstIndex);
    }

    @FunctionalInterface
    public interface ForEachKeyAndValue<K, V, P1, P2> {

        void each(K key, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2);
    }

    BaseExponentMap(int initialCapacityExponent, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseExponentMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);
    }
}
