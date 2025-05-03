package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

abstract class BaseObjectWithRemoveNonBucketMap<K, V> extends BaseObjectNonContainsKeyNonBucketMap<K, V> {

    BaseObjectWithRemoveNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValues) {
        super(initialCapacityExponent, createKeyArray, createValues);
    }

    BaseObjectWithRemoveNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray,
            IntFunction<V[]> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, createValues);
    }

    BaseObjectWithRemoveNonBucketMap(BaseObjectWithRemoveNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(toCopy, copyValuesContent);
    }
}
